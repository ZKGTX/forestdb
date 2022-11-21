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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SubjectBarChartExcelExporter {

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private Subject subject;

    private List<Risk> risks;

    private List<String> messages;


    public SubjectBarChartExcelExporter(Subject subject, List<Risk> risks) {
        this.subject = subject;
        this.risks = risks;
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
        createCell(row, 0, "Климатический риск", style);
        createCell(row, 1, "Общий % выполнения мероприятий", style);
        createCell(row, 2, "Общее кол-во мероприятий", style);
        createCell(row, 3, "Примечание", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        else {
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
        for (Risk risk : risks) {
            messages = new ArrayList<>();
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, risk.getName(), style);
            createCell(row, columnCount++, totalCompletionPercentage(risk, year), style);
            createCell(row, columnCount++, totalNumberOfActions(risk, year), style);
            createCell(row, columnCount++, messages.size() != 0 ? messages.toString() : "", style);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
        }
    }

    private void createBarChart () {
        int range = subject.getRisks().size();

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0,0, 0, range+1, 4, range+25);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(subject.getName() + " : процент выполнения мероприятий");
        chart.setTitleOverlay(false);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Климатический риск");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("% выполнения");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        XDDFDataSource<String> names = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, range,0, 0));

        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, range, 1,1));

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFChartData.Series series = data.addSeries(names, values);
        series.setTitle("Риск", null);
        data.setVaryColors(true);
        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
    }

    public void export(HttpServletResponse response) throws IOException {
        Set<Integer> years = getYears(getReportingYears(getActions(getMeasures(risks))));
        for (Integer year : years) {
            writeHeaders(year);
            writeDataRows(year);
            createBarChart();
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    public Double totalCompletionPercentage(Risk risk, Integer year) {
        List<Action> actions = getActions(risk.getMeasures());
        BigDecimal numerator = BigDecimal.ZERO;
        BigDecimal denominator = BigDecimal.ZERO;
        for (Action action : actions) {
            numerator = numerator.add(calculateNumeratorPart(action, year));
            denominator = denominator.add(calculateDenominatorPart(action, year));
        }
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal totalCompletionRate = numerator.divide(denominator, 1, RoundingMode.UP);
        BigDecimal hundredPercent = new BigDecimal("100");
        return totalCompletionRate.compareTo(hundredPercent) >= 0 ? hundredPercent.doubleValue() : totalCompletionRate.doubleValue();
    }

    public int totalNumberOfActions(Risk risk, Integer year) {
        List<Action> actions = getActions(risk.getMeasures());
        int result = 0;
        for (Action action : actions) {
            List<ReportingYear> reportingYears = action.getReportingYears();
            for (ReportingYear reportingYear : reportingYears) {
                if (Objects.equals(reportingYear.getYear(), year)) {
                    result++;
                }
            }
        }
        return result;
    }

    public BigDecimal calculateNumeratorPart(Action action, Integer year) throws ArithmeticException, NumberFormatException {
        List <ReportingYear> reportingYears = action.getReportingYears();

        for (ReportingYear reportingYear : reportingYears) {

            if (Objects.equals(reportingYear.getYear(), year)) {
                BigDecimal plannedWorkValue = BigDecimal.ZERO;
                BigDecimal actualWorkValue = BigDecimal.ZERO;

                try {
                    plannedWorkValue = plannedWorkValue.add(cleanValue(reportingYear.getPlannedWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add(reportingYear.getAction().getName() + " : данные по плановому объему за " + year + " год отсутствуют");
                }

                try {
                    actualWorkValue = actualWorkValue.add(cleanValue(reportingYear.getActualWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add(reportingYear.getAction().getName() + ": данные по фактическому объему за " + year + " год отсутствуют");
                }

                if (plannedWorkValue.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO;
                }

                BigDecimal hundredPercent = new BigDecimal("100");

                BigDecimal actionCompletionRate = actualWorkValue.divide(plannedWorkValue, 2, RoundingMode.HALF_UP);

                BigDecimal actionCompletionPercentage = actionCompletionRate.multiply(hundredPercent).setScale(1, RoundingMode.UP);

                int completionPercentageEvaluation = actionCompletionPercentage.compareTo(hundredPercent);

                if (completionPercentageEvaluation >= 0) {
                    return plannedWorkValue.multiply(hundredPercent).setScale(1, RoundingMode.UP);
                }

                return plannedWorkValue.multiply(actionCompletionPercentage).setScale(1, RoundingMode.UP);
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateDenominatorPart(Action action, Integer year) {
        List<ReportingYear> reportingYears = action.getReportingYears();
        for (ReportingYear reportingYear : reportingYears) {
            if (Objects.equals(reportingYear.getYear(), year)) {
                try {
                    return cleanValue(reportingYear.getPlannedWorkAmount()).setScale(1, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                }
            }
        }
        return BigDecimal.ZERO;
    }


    public List<Measure> getMeasures (List<Risk> risks) {
        List<Measure> measures = new ArrayList<>();
        for (Risk risk : risks) {
            measures.addAll(risk.getMeasures());
        }
        return measures;
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

    public BigDecimal cleanValue (String rawValue) throws NumberFormatException {
        String cleanValue = convertCommasToDots(removeStars(rawValue));
        return new BigDecimal(cleanValue);
    }

    public String convertCommasToDots (String s) {
        return s.replaceAll("[,]+",".");
    }

    public String removeStars (String s) {
        return s.replaceAll("[*]+", "");
    }


}






