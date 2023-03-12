package com.zerokikr.forestdb.export;

import com.zerokikr.forestdb.entity.*;
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

public class AllSubjectsPercentageExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Subject> subjects;
    private List<String> messages;

    public AllSubjectsPercentageExcelExporter(List<Subject> subjects) {
        this.subjects = subjects;
        workbook = new XSSFWorkbook();
    }

    private void writeAllHeaders (Integer year) {
        sheet = workbook.createSheet(year.toString());
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setWrapText(true);
        createCell(row, 0, "Субъект РФ", style);
        createCell(row, 1, "% выполненных мероприятий", style);
        createCell(row, 2, "Примечание", style);
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

    private void createCell(Row row, int columnCount, Object value, int customHeight, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.getRow().setHeightInPoints(cell.getSheet().getDefaultRowHeightInPoints() * customHeight);
        cell.setCellStyle(style);
    }

    private void writeAllDataRows(Integer year) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        style.setWrapText(true);
        for (Subject subject : subjects) {
            messages = new ArrayList<>();
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, subject.getName(), style);
            createCell(row, columnCount++, completionPercentageByComparison(subject, year), style);
            createCell(row, columnCount++, messages.size() != 0 ? printMessages(messages) : "", messages.size() + 1, style);
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    public void exportAll(HttpServletResponse response) throws IOException {
        Set<Integer> years = getYears(getReportingYears(getActions(getMeasures(getRisksFromAll(subjects)))));
        for (Integer year : years) {
            writeAllHeaders(year);
            writeAllDataRows(year);
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    public String completionPercentageByComparison (Subject subject, Integer year) {
        List<Action> allActions = getActions(getMeasures(getRisksFromOne(subject)));
        List<Action> completedActions = new ArrayList<>();
        Double zero = Double.valueOf(0);
        Double fifty = Double.valueOf(50);
        Double oneHundred = Double.valueOf(100);
        BigDecimal hundredPercent = new BigDecimal("100");
        for (Action action : allActions) {
            BigDecimal actionCompletionPercentage = completionPercentage(action, year);
            int evaluation = actionCompletionPercentage.compareTo(hundredPercent);
            if (evaluation >= 0) {
                completedActions.add(action);
            }
        }
        Double x = (double) completedActions.size();
        Double y = (double) allActions.size();
        Double rate = x/y;
        Double percent = rate * 100;


        if (percent.compareTo(zero) == 0) {
            return "0% мероприятий выполнены";
        }

        if (percent.compareTo((oneHundred)) == 0) {
            return "100% мероприятий выполнены";
        }

        if (percent.compareTo((fifty)) < 0 &&
                percent.compareTo((zero)) >= 0) {
            return "менее 50% мероприятий выполнены";
        }

        if (percent.compareTo((fifty)) >= 0 &&
                percent.compareTo((oneHundred)) < 0) {
            return "более 50% мероприятий выполнены";
        }
        return "";
    }


    public BigDecimal completionPercentage(Action action, Integer year) {
        List <ReportingYear> reportingYears = action.getReportingYears();
        BigDecimal completionPercentage = BigDecimal.ZERO;
        for (ReportingYear reportingYear : reportingYears) {

            if (Objects.equals(reportingYear.getYear(), year)) {

                BigDecimal plannedWorkValue = BigDecimal.ZERO;
                BigDecimal actualWorkValue = BigDecimal.ZERO;

                try {
                    plannedWorkValue = plannedWorkValue.add(cleanValue(reportingYear.getPlannedWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add(action.getName() + ": цифры планового объема за " + year + " год отсутствуют");
                }

                try {
                    actualWorkValue = actualWorkValue.add(cleanValue(reportingYear.getActualWorkAmount())).setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    messages.add(action.getName() + ": цифры фактического объема за " + year + " год отсутствуют");
                }

                if (plannedWorkValue.compareTo(BigDecimal.ZERO) == 0) {
                    return plannedWorkValue;
                }

                BigDecimal actionCompletionRate = actualWorkValue.divide(plannedWorkValue, 2, RoundingMode.HALF_UP);

                BigDecimal hundredPercent = new BigDecimal("100");

                completionPercentage = completionPercentage.add(actionCompletionRate.multiply(hundredPercent)).setScale(1, RoundingMode.UP);

                return completionPercentage;
            }
        }
        messages.add(action.getName() + ": данные за " + year + " год отсутствуют");
        return completionPercentage;
    }

    public List<Risk> getRisksFromAll(List<Subject> subjects) {
        List<Risk> risks = new ArrayList<>();
        for (Subject subject : subjects) {
            risks.addAll(subject.getRisks());
        }
        return risks;
    }

    public List<Risk> getRisksFromOne (Subject subject) {
        return new ArrayList<>(subject.getRisks());
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

    public String printMessages (List<String>messages) {
        StringBuilder sb = new StringBuilder();
        for (String s : messages) {
            sb.append(s);
            sb.append(";");
            sb.append("\n");
        }
        return sb.toString();
    }




}
