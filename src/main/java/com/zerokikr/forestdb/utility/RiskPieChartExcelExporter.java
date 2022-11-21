package com.zerokikr.forestdb.utility;

import com.zerokikr.forestdb.entity.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class RiskPieChartExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Risk risk;
    private List<Measure> measures;


    public RiskPieChartExcelExporter(Risk risk, List<Measure> measures) {
        this.risk = risk;
        this.measures = measures;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaders(Integer year) {
            sheet = workbook.createSheet(year.toString());
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(14);
            style.setFont(font);
            createCell(row, 0, "Мероприятие", style);
            createCell(row, 1, "Стоимость, руб.", style);
            createCell(row, 2, "Примечание", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataRows(Integer year) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        List<Action> actions = getActions(measures);
        for (Action action : actions) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, action.getName(), style);
            try {
                createCell(row, columnCount++, yearWorkCost(action, year), style);
            } catch (NumberFormatException nFE) {
                createCell(row, columnCount++, "данные за "  + year + " год отсутствуют", style);
            }
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

    }

    private void createPieChart () {
        int range = getActions(measures).size();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0,0, 0, range+1, 5, range+25);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(risk.getSubject().getName() + "\n" + risk.getName() + " : сравнительная стоимость мероприятий");
        chart.setTitleOverlay(false);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);
        XDDFDataSource<String> names = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, range,0, 0));
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, range, 1,1));
        XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);
        data.setVaryColors(true);
        data.addSeries(names, values);
        chart.plot(data);
    }

    public void export(HttpServletResponse response) throws IOException {
        Set<Integer> years = getYears(getReportingYears(getActions(measures)));
        for (Integer year : years) {
            writeHeaders(year);
            writeDataRows(year);
            createPieChart();
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

    }

    public List<Action> getActions(List<Measure> measures) {
        List<Action> actions = new ArrayList<>();
        for (Measure m : measures) {
            actions.addAll(m.getActions());
        }
        return actions;
    }

    public List<ReportingYear> getReportingYears(List<Action>actions) {
        List<ReportingYear> reportingYears = new ArrayList<>();
        for (Action a : actions) {
            reportingYears.addAll(a.getReportingYears());
        }
        return reportingYears;
    }

    public Set<Integer> getYears(List<ReportingYear> reportingYears) {
        Set<Integer> years = new HashSet<>();
        for (ReportingYear rYear : reportingYears) {
            years.add(rYear.getYear());
        }
        return years;
    }

    public Double yearWorkCost (Action action, Integer yearValue) throws NumberFormatException {
        List <ReportingYear> reportingYears = action.getReportingYears();
        for (ReportingYear reportingYear : reportingYears) {
            if (Objects.equals(reportingYear.getYear(), yearValue)) {
                Double result = cleanValue(reportingYear.getActualWorkCost());
                if(reportingYear.getCostMeasuringUnits().equals("тыс. руб.")) {
                    return result * 1000;
                }
                return result;
            }
        }
        return 0.0;
    }

    public Double cleanValue (String rawValue) throws NumberFormatException {
        String cleanValue = removeStars(rawValue);
        return Double.parseDouble(convertCommasToDots(cleanValue));
    }

    public String convertCommasToDots (String s) {
        return s.replaceAll("[,]+",".");
    }

    public String removeStars (String s) {
        return s.replaceAll("[*]+", "");
    }
}
