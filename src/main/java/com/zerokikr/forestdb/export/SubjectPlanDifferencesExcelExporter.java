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

//public class SubjectPlanDifferencesExcelExporter {
//
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//    private Subject subject;
//    private List<Risk> risks;
//
//    private List<String> messages;
//
//    public SubjectPlanDifferencesExcelExporter(Subject subject, List<Risk> risks) {
//        this.subject = subject;
//        this.risks = risks;
//        workbook = new XSSFWorkbook();
//    }
//
//    private void writeHeaders(Integer year) {
//        sheet = workbook.createSheet(year.toString());
//        Row row = sheet.createRow(0);
//        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(14);
//        style.setFont(font);
//        createCell(row, 0, "наименование мероприятия", style);
//        createCell(row, 1, "план по Лесному плану", style);
//        createCell(row, 2, "план по присланным материалам", style);
//        createCell(row, 3, "примечание", style);
//    }
//
//    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        Cell cell = row.createCell(columnCount);
//        if (value instanceof Double) {
//            cell.setCellValue((Double) value);
//        } else {
//            cell.setCellValue((String) value);
//        }
//        cell.setCellStyle(style);
//    }
//
//    private void createCell(Row row, int columnCount, Object value, int customHeight, CellStyle style) {
//        Cell cell = row.createCell(columnCount);
//        if (value instanceof Double) {
//            cell.setCellValue((Double) value);
//        } else {
//            cell.setCellValue((String) value);
//        }
//        cell.getRow().setHeightInPoints(cell.getSheet().getDefaultRowHeightInPoints() * customHeight);
//        cell.setCellStyle(style);
//    }
//
//    private void writeDataRows(Integer year) {
//        int rowCount = 1;
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(10);
//        style.setFont(font);
//        style.setWrapText(true);
//        List<Action> actions = getActions(getMeasures(risks));
//        for (Action action : actions) {
//            messages = new ArrayList<>();
//            Row row = sheet.createRow(rowCount++);
//            int columnCount = 0;
//            createCell(row, columnCount++, action.getName(), style);
//            createCell(row, columnCount++, getForestWorkPlan(action, year), style);
//            createCell(row, columnCount++, getSentWorkPlan(action, year), style);
//            createCell(row, columnCount++, messages.size() != 0 ? printMessages(messages) : "", messages.size() + 1, style);
//
//        }
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//    }
//
//    public Double getForestWorkPlan(Action action, Integer year) {
//        List<ReportingYear> reportingYears = action.getReportingYears();
//
//        for (ReportingYear reportingYear : reportingYears) {
//
//            if (reportingYear.getYear().equals(year)) {
//                BigDecimal forestWorkPlan = BigDecimal.ZERO;
//                BigDecimal sentWorkPlan = BigDecimal.ZERO;
//                try {
//                    forestWorkPlan = forestWorkPlan.add(cleanValue(reportingYear.getTrueWorkPlan()));
//                } catch (NumberFormatException e) {
//                    messages.add("цифры лесного плана за " + year + " год отсутствуют");
//                }
//                try {
//                    sentWorkPlan = sentWorkPlan.add(cleanValue(reportingYear.getPlannedWorkAmount()));
//                } catch (NumberFormatException e) {
//                    messages.add("цифры присланного регионом плана за " + year + " год отсутствуют");
//                }
//                if (forestWorkPlan.compareTo(sentWorkPlan) != 0) {
//                    messages.add("планы НЕ РАВНЫ");
//                    return forestWorkPlan.doubleValue();
//                } else {
//                    messages.add("планы равны");
//                    return forestWorkPlan.doubleValue();
//                }
//            }
//        }
//        messages.add("данные за " + year + " отсутсвуют");
//        return 0.0;
//    }
//
//    public Double getSentWorkPlan(Action action, Integer year) {
//        List<ReportingYear> reportingYears = action.getReportingYears();
//
//        for (ReportingYear reportingYear : reportingYears) {
//
//            if (reportingYear.getYear().equals(year)) {
//                try {
//                    return cleanValue(reportingYear.getPlannedWorkAmount()).doubleValue();
//                } catch (NumberFormatException e) {
//
//                }
//            }
//        }
//        return 0.0;
//    }
//
//    public void export(HttpServletResponse response) throws IOException {
//        Set<Integer> years = getYears(getReportingYears(getActions(getMeasures(risks))));
//        for (Integer year : years) {
//            writeHeaders(year);
//            writeDataRows(year);
//        }
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        outputStream.close();
//        workbook.close();
//    }
//
//    public List<Measure> getMeasures (List<Risk> risks) {
//        List<Measure> measures = new ArrayList<>();
//        for (Risk risk : risks) {
//            measures.addAll(risk.getMeasures());
//        }
//        return measures;
//    }
//
//    public List<Action> getActions(List<Measure> measures) {
//        List<Action> actions = new ArrayList<>();
//        for (Measure m : measures) {
//            actions.addAll(m.getActions());
//        }
//        return actions;
//    }
//
//    public List<ReportingYear> getReportingYears(List<Action>actions) {
//        List<ReportingYear> reportingYears = new ArrayList<>();
//        for (Action a : actions) {
//            reportingYears.addAll(a.getReportingYears());
//        }
//        return reportingYears;
//    }
//
//    public Set<Integer> getYears(List<ReportingYear> reportingYears) {
//        Set<Integer> years = new HashSet<>();
//        for (ReportingYear rYear : reportingYears) {
//            years.add(rYear.getYear());
//        }
//        return years;
//    }
//
//    public BigDecimal cleanValue (String rawValue) throws NumberFormatException, NullPointerException {
//        String cleanValue = convertCommasToDots(removeStars(rawValue));
//        return new BigDecimal(cleanValue);
//    }
//
//    public String convertCommasToDots (String s) {
//        return s.replaceAll("[,]+",".");
//    }
//
//    public String removeStars (String s) throws NullPointerException {
//        return s.replaceAll("[*]+", "");
//    }
//
//    public String printMessages (List<String>messages) {
//        StringBuilder sb = new StringBuilder();
//        for (String s : messages) {
//            sb.append(s);
//            sb.append(";");
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
//
//}
