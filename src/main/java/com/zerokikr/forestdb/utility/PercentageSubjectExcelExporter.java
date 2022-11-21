package com.zerokikr.forestdb.utility;

import com.zerokikr.forestdb.entity.*;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PercentageSubjectExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Subject subject;
    private List<Risk> risks;

    private List<String> messages;

    public PercentageSubjectExcelExporter(Subject subject, List<Risk> risks) {
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
        createCell(row, 0, "Наименование мероприятия", style);
        createCell(row, 1, "% выполнения", style);
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
        List<Action> actions = getActions(getMeasures(risks));
        for (Action action : actions) {
            messages = new ArrayList<>();
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, action.getName(), style);
            createCell(row, columnCount++, completionPercentage(action, year), style);
            createCell(row, columnCount++, messages.size() != 0 ? messages.toString() : "", style);
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

    }

    public Double completionPercentage(Action action, Integer year) {
        List <ReportingYear> reportingYears = action.getReportingYears();
        BigDecimal completionPercentage = BigDecimal.ZERO;
        for (ReportingYear reportingYear : reportingYears) {

            if (Objects.equals(reportingYear.getYear(), year)) {

                BigDecimal plannedWorkValue = BigDecimal.ZERO;
                BigDecimal actualWorkValue = BigDecimal.ZERO;

                try {
                    plannedWorkValue = plannedWorkValue.add(cleanValue(reportingYear.getPlannedWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add("данные по плановому объему за " + year + " год отсутствуют");
                }

                try {
                    actualWorkValue = actualWorkValue.add(cleanValue(reportingYear.getActualWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add("данные по фактическому объему за " + year + " год отсутствуют");
                }

                if (plannedWorkValue.compareTo(BigDecimal.ZERO) == 0) {
                    return plannedWorkValue.doubleValue();
                }

                BigDecimal actionCompletionRate = actualWorkValue.divide(plannedWorkValue, 2, RoundingMode.HALF_UP);

                BigDecimal hundredPercent = new BigDecimal("100");

                completionPercentage = completionPercentage.add(actionCompletionRate.multiply(hundredPercent)).setScale(1, RoundingMode.UP);

                return completionPercentage.doubleValue();
            }
        }
        messages.add("данные за " + year + " год отсутствуют");
        return 0.0;
    }

    public void export(HttpServletResponse response) throws IOException {
        Set<Integer> years = getYears(getReportingYears(getActions(getMeasures(risks))));
        for (Integer year : years) {
            writeHeaders(year);
            writeDataRows(year);
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
