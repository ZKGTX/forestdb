package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.service.RiskService;
import com.zerokikr.forestdb.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RiskController {

    private RiskService riskService;

    private SubjectService subjectService;

    public RiskController(RiskService riskService, SubjectService subjectService) {
        this.riskService = riskService;
        this.subjectService = subjectService;
    }

    @InitBinder
    public void initBinder (WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/risks")
    public String showAllRisksBySubjectId(Model theModel, @RequestParam("subjectId") Long subjectId) {
        List<Risk> allRisks = riskService.getAllRisksBySubjectId(subjectId);
        Subject subject = subjectService.getSubjectById(subjectId);
        theModel.addAttribute("allRisks", allRisks);
        theModel.addAttribute("subject", subject);
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
        riskService.saveRisk(risk);
        theModel.addAttribute("subjectId", risk.getSubject().getId());
        return new ModelAndView("redirect:/risks", theModel);
    }

}
