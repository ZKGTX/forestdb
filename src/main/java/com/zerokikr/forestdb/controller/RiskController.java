package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.repository.specification.RiskSpecs;
import com.zerokikr.forestdb.service.MeasureService;
import com.zerokikr.forestdb.service.RiskService;
import com.zerokikr.forestdb.service.SubjectService;
import com.zerokikr.forestdb.export.RiskPieChartExcelExporter;
import com.zerokikr.forestdb.utility.TimeNameSetter;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RiskController {

    private RiskService riskService;

    private SubjectService subjectService;

    private MeasureService measureService;


    public RiskController(RiskService riskService, SubjectService subjectService, MeasureService measureService) {
        this.riskService = riskService;
        this.subjectService = subjectService;
        this.measureService = measureService;
    }

    @InitBinder
    public void initBinder (WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/risks")
    public String showAllRisksBySubjectId(Model theModel, @RequestParam("subjectId") Long subjectId, @RequestParam(value = "keyword", required = false) String keyword) {
        Specification<Risk> spec = Specification.where(RiskSpecs.subjectIdEqualsTo(subjectId));
        Subject subject = subjectService.getSubjectById(subjectId);
        if (keyword != null) {
            String theKeyword = keyword.toLowerCase();
            spec = spec.and(RiskSpecs.nameContains(theKeyword));
        }
        List<Risk> allRisks = riskService.getRisksBySubjectIdAndKeyword(spec);
        theModel.addAttribute("allRisks", allRisks);
        theModel.addAttribute("subject", subject);
        theModel.addAttribute("keyword", keyword);
        return "risks/risks";
    }

    @GetMapping("/risks/add")
    public String showAddForm (Model theModel, @RequestParam("subjectId") Long subjectId) {
        Risk risk = new Risk();
        Subject subject = subjectService.getSubjectById(subjectId);
        risk.setSubject(subject);
        theModel.addAttribute("risk", risk);
        return "risks/addRisk";
    }

    @GetMapping("/risks/update")
    public String showUpdateForm(@RequestParam("riskId") Long id, Model theModel) {
        Risk risk = riskService.getRiskById(id);
        theModel.addAttribute("risk", risk);
        return "risks/addRisk";
    }

    @GetMapping("risks/delete")
    public ModelAndView delete(ModelMap theModel, @RequestParam("riskId") Long riskId, @RequestParam("subjectId") Long subjectId) {
        riskService.deleteRiskById(riskId);
        theModel.addAttribute("subjectId", subjectId);
        return new ModelAndView("redirect:/risks", theModel);
    }

    @PostMapping("/risks/save")
    public ModelAndView saveRisk(ModelMap theModel, @ModelAttribute("risk") @Valid Risk risk, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView( "risks/addRisk");
        }
        risk.setLastUpdate(TimeNameSetter.setTimeName());
        riskService.saveRisk(risk);
        theModel.addAttribute("subjectId", risk.getSubject().getId());
        return new ModelAndView("redirect:/risks", theModel);
    }

    @GetMapping("/risks/reports")
    public String showAllReportsByRiskId(Model theModel, @RequestParam("riskId") Long riskId) {
        Risk risk = riskService.getRiskById(riskId);
        theModel.addAttribute("risk", risk);
        return "reports/riskReports";
    }

    @GetMapping("risks/export")
    public void exportToExcel (HttpServletResponse response, @RequestParam ("riskId") Long riskId, RedirectAttributes redirectAttributes) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        Risk risk = riskService.getRiskById(riskId);
        List<Measure> measures = measureService.getAllMeasuresByRiskId(riskId);

        String fileName = risk.getName() + "_стоимость_мероприятий_";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String correctFileName = encodedFileName.replace('+', ' ');
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+ correctFileName + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        RiskPieChartExcelExporter riskPieChartExcelExporter = new RiskPieChartExcelExporter(risk, measures);
        try {
            riskPieChartExcelExporter.export(response);
        } catch (NullPointerException e) {
            redirectAttributes.addFlashAttribute("exportError", "Не удается сгенертровать отчет. Скорее всего нет данных об одном или нескольких мероприятиях");
        }
    }
}
