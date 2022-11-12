package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.service.RiskService;
import com.zerokikr.forestdb.service.SubjectService;
import com.zerokikr.forestdb.utility.SubjectBarChartExcelExporter;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private SubjectService subjectService;

    private RiskService riskService;

    public SubjectController(SubjectService subjectService, RiskService riskService) {
        this.subjectService = subjectService;
        this.riskService = riskService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping()
    public String showAllSubjects(Model theModel) {
        List<Subject> allSubjects = (List<Subject>) subjectService.getAllSubjects();
        theModel.addAttribute("allSubjects", allSubjects);
        return "subjects/subjects";
    }

    @GetMapping("/add")
    public String showAddForm(Model theModel) {
        Subject subject = new Subject();
        theModel.addAttribute("subject", subject);
        return "subjects/addSubject";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("subjectId") Long id, Model theModel) {
        Subject subject = subjectService.getSubjectById(id);
        theModel.addAttribute("subject", subject);
        return "subjects/addSubject";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("subjectId") Long subjectId) {
        subjectService.deleteSubjectById(subjectId);
        return "redirect:/subjects";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") @Valid Subject subject, BindingResult result) {
        if (result.hasErrors()) {
            return "subjects/addSubject";
        }
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/reports")
    public String showAllReportsBySubjectId (Model theModel, @RequestParam ("subjectId") long subjectId) {

        Subject subject = subjectService.getSubjectById(subjectId);

        theModel.addAttribute("subject", subject);

        return "reports/subjectReports";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response, @RequestParam("subjectId") Long subjectId) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=subject_bar_chart_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        Subject subject = subjectService.getSubjectById(subjectId);
        List<Risk> risks = riskService.getAllRisksBySubjectId(subjectId);

        SubjectBarChartExcelExporter subjectBarChartExcelExporter = new SubjectBarChartExcelExporter(subject, risks);
        subjectBarChartExcelExporter.export(response);

    }

}
