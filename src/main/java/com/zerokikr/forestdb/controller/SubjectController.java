package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.service.SubjectService;
import com.zerokikr.forestdb.utility.SubjectExcelExporter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping()
    public String showAllSubjects(Model theModel) {
        List<Subject> allSubjects = (List<Subject>) subjectService.getAllSubjects();
        theModel.addAttribute("allSubjects", allSubjects);
        return "subjects/subjects";
    }

    @GetMapping("/add")
    public String showAddForm (Model theModel) {
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
    public String saveSubject(@ModelAttribute("subject") Subject subject) {
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Subject> subjects = subjectService.getAllSubjects();

        SubjectExcelExporter excelExporter = new SubjectExcelExporter(subjects);

        excelExporter.export(response);
    }



}
