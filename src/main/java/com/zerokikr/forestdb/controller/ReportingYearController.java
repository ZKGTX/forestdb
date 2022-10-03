package com.zerokikr.forestdb.controller;


import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.service.ActionService;
import com.zerokikr.forestdb.service.ReportingYearService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class ReportingYearController {

   private ReportingYearService reportingYearService;

   private ActionService actionService;


    public ReportingYearController(ReportingYearService reportingYearService, ActionService actionService) {
        this.reportingYearService = reportingYearService;
        this.actionService = actionService;
    }

    @InitBinder
    public void initBinder (WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

    }

    @GetMapping("/years")
    public String showAllYearsByActionId(Model theModel, @RequestParam("actionId") Long actionId) {
        List<ReportingYear> allReportingYears = reportingYearService.getAllReportingYearsByActionId(actionId);
        Action action = actionService.getActionById(actionId);
        theModel.addAttribute("allReportingYears", allReportingYears);
        theModel.addAttribute("action", action);
        return "years/years";
    }

    @GetMapping("/years/add")
    public String showAddForm (Model theModel, @RequestParam("actionId") Long actionId) {
        ReportingYear reportingYear = new ReportingYear();
        Action action = actionService.getActionById(actionId);
        reportingYear.setAction(action);
        theModel.addAttribute("reportingYear", reportingYear);
        return "years/addYear";
    }

    @PostMapping("/years/save")
    public ModelAndView saveReportingYear(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/addYear");
        }
        reportingYearService.saveReportingYear(reportingYear);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());
        return new ModelAndView("redirect:/years", theModel);
    }

    @GetMapping("/years/update")
    public String showUpdateForm(@RequestParam("reportingYearId") Long id, Model theModel) {
        ReportingYear reportingYear = reportingYearService.getReportingYearById(id);
        theModel.addAttribute("reportingYear", reportingYear);
        return "years/addYear";
    }

    @GetMapping("years/delete")
    public ModelAndView delete(ModelMap theModel, @RequestParam("reportingYearId") Long reportingYearId, @RequestParam("actionId") Long actionId) {
        reportingYearService.deleteReportingYearById(reportingYearId);
        theModel.addAttribute("actionId", actionId);
        return new ModelAndView("redirect:/years", theModel);
    }




}
