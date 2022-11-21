package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.repository.specification.SubjectSpecs;
import com.zerokikr.forestdb.service.RiskService;
import com.zerokikr.forestdb.service.SubjectService;
import com.zerokikr.forestdb.utility.PercentageSubjectExcelExporter;
import com.zerokikr.forestdb.utility.SubjectBarChartExcelExporter;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public String showAllSubjects(Model theModel, @RequestParam(value = "keyword", required = false) String keyword) {
        Specification<Subject> spec = Specification.where(null);
        if (keyword != null) {
            spec = spec.and(SubjectSpecs.nameContains(keyword));
        }
        List<Subject> allSubjects = subjectService.getSubjectsByKeyword(spec);
        theModel.addAttribute("allSubjects", allSubjects);
        theModel.addAttribute("keyword", keyword);
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
    public void exportToExcel(HttpServletResponse response, @RequestParam("subjectId") Long subjectId, RedirectAttributes redirectAttributes) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        Subject subject = subjectService.getSubjectById(subjectId);
        List<Risk> risks = riskService.getAllRisksBySubjectId(subjectId);

        String fileName = subject.getName() + "_общий_процент_выполнения_мероприятий_";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String correctFileName = encodedFileName.replace('+', ' ');

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+ correctFileName + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        SubjectBarChartExcelExporter subjectBarChartExcelExporter = new SubjectBarChartExcelExporter(subject, risks);

        subjectBarChartExcelExporter.export(response);
    }

    @GetMapping("/exportPercents")
    public void exportPercentsToExcel(HttpServletResponse response, @RequestParam("subjectId") Long subjectId, RedirectAttributes redirectAttributes) throws IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        Subject subject = subjectService.getSubjectById(subjectId);
        List<Risk> risks = riskService.getAllRisksBySubjectId(subjectId);

        String fileName = subject.getName() + "_процент_выполнения_мероприятий_";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String correctFileName = encodedFileName.replace('+', ' ');

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+ correctFileName + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        PercentageSubjectExcelExporter percentageSubjectExcelExporter = new PercentageSubjectExcelExporter(subject, risks);

        percentageSubjectExcelExporter.export(response);
    }

}
