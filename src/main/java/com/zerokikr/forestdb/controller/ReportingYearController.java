package com.zerokikr.forestdb.controller;


import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.repository.specification.ReportingYearSpecs;
import com.zerokikr.forestdb.service.ActionService;
import com.zerokikr.forestdb.service.ReportingYearService;
import com.zerokikr.forestdb.utility.TimeNameSetter;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public String showAllYearsByActionId(Model theModel, @RequestParam("actionId") Long actionId,
                                         @RequestParam("original") Boolean original, @RequestParam(value = "year", required = false) Integer year) {
        if (original) {
            Specification<ReportingYear> spec = Specification.where(ReportingYearSpecs.actionIdEqualsTo(actionId));
            if (year != null) {
                spec = spec.and(ReportingYearSpecs.yearEqualsTo(year));
            }

            Action action = actionService.getActionById(actionId);

            List<ReportingYear> allReportingYears = reportingYearService.getAllReportingYearsByActionIdAndKeyword(spec);

            theModel.addAttribute("allReportingYears", allReportingYears);
            theModel.addAttribute("action", action);
            theModel.addAttribute("year", year);
            return "years/originalYears";
        }
        Specification<ReportingYear> spec = Specification.where(ReportingYearSpecs.actionIdEqualsTo(actionId));
        if (year != null) {
            spec = spec.and(ReportingYearSpecs.yearEqualsTo(year));
        }

        Action action = actionService.getActionById(actionId);

        List<ReportingYear> allReportingYears = reportingYearService.getAllReportingYearsByActionIdAndKeyword(spec);

        theModel.addAttribute("allReportingYears", allReportingYears);
        theModel.addAttribute("action", action);
        theModel.addAttribute("year", year);
        return "years/years";
    }

    @GetMapping("/years/add")
    public String showAddForm (Model theModel, @RequestParam("actionId") Long actionId) {
        ReportingYear reportingYear = new ReportingYear();
        Action action = actionService.getActionById(actionId);
        reportingYear.setAction(action);
        theModel.addAttribute("original", false);
        theModel.addAttribute("reportingYear", reportingYear);
        return "years/addYear";
    }

    @GetMapping("/years/addNew")
    public String showAddNewForm (Model theModel, @RequestParam("actionId") Long actionId) {
        ReportingYear reportingYear = new ReportingYear();
        Action action = actionService.getActionById(actionId);
        reportingYear.setAction(action);
        theModel.addAttribute("original", true);
        theModel.addAttribute("reportingYear", reportingYear);
        return "years/addNewYear";
    }

    @PostMapping("/years/save")
    public ModelAndView saveReportingYear(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {
        theModel.addAttribute("original", false);

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/addYear");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());
        theModel.addAttribute("original", false);
        return new ModelAndView("redirect:/years", theModel);
    }

    @PostMapping("/years/saveNew")
    public ModelAndView saveNewReportingYear(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {
        theModel.addAttribute("original", true);

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/addNewYear");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());
        return new ModelAndView("redirect:/years", theModel);
    }

    @PostMapping("/years/doUpdate")
    public ModelAndView updateReportingYear(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {
        theModel.addAttribute("original", false);

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/addYear");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());
        return new ModelAndView("redirect:/years", theModel);
    }

    @PostMapping("/years/doUpdateNew")
    public ModelAndView updateNewReportingYear(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {
        theModel.addAttribute("original", true);

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/addYear");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());
        return new ModelAndView("redirect:/years", theModel);
    }

    @PostMapping("/years/doUpdateYear")
    public ModelAndView updateReportingYearByUser(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {

        String plannedWorkAmount = reportingYear.getPlannedWorkAmount();
        String actualWorkAmount = reportingYear.getActualWorkAmount();
        String comment = reportingYear.getCommentary();

        Boolean original = false;
        theModel.addAttribute("original", original);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());

        try {
            String workError = reportingYearService.workAmountsDiffer(plannedWorkAmount, actualWorkAmount, comment);
            if (!workError.isEmpty()) {
                ObjectError amountsDiffer = new ObjectError("workError", workError);
                bindingResult.addError(amountsDiffer);
            }
        } catch (NumberFormatException | NullPointerException e) {
            String noDigitsError = "отсутствуют цифры планового и/или фактического объема работ";
            ObjectError noDigits = new ObjectError("noDigitsError", noDigitsError);
            bindingResult.addError(noDigits);
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/updateYearByUser");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);

        return new ModelAndView("redirect:/years", theModel);
    }

    @GetMapping("/years/updateYear")
    public String showUpdateYearForm(@RequestParam("reportingYearId") Long id, Model theModel) {
        ReportingYear reportingYear = reportingYearService.getReportingYearById(id);
        Boolean original = false;
        theModel.addAttribute("original", original);
        theModel.addAttribute("reportingYear", reportingYear);
        return "years/updateYearByUser";
    }


    @GetMapping("/years/update")
    public String showUpdateForm(@RequestParam("reportingYearId") Long id, Model theModel) {
        ReportingYear reportingYear = reportingYearService.getReportingYearById(id);
        theModel.addAttribute("reportingYear", reportingYear);
        theModel.addAttribute("original", false);
        return "years/updateYear";
    }

    @GetMapping("/years/updateNew")
    public String showNewUpdateForm(@RequestParam("reportingYearId") Long id, Model theModel) {
        ReportingYear reportingYear = reportingYearService.getReportingYearById(id);
        theModel.addAttribute("reportingYear", reportingYear);
        theModel.addAttribute("original", true);
        return "years/updateNewYear";
    }

    @GetMapping("years/delete")
    public ModelAndView delete(ModelMap theModel, @RequestParam("reportingYearId") Long reportingYearId, @RequestParam("actionId") Long actionId) {
        reportingYearService.deleteReportingYearById(reportingYearId);
        theModel.addAttribute("actionId", actionId);
        theModel.addAttribute("original", actionService.getActionById(actionId).getOriginal());
        return new ModelAndView("redirect:/years", theModel);
    }

    @GetMapping ("years/updatePlans")
    public String showUpdatePlans (@RequestParam ("reportingYearId") Long id, Model theModel) {
        ReportingYear reportingYear = reportingYearService.getReportingYearById(id);
        reportingYear.setCurrentWork(reportingYear.getPlannedWorkAmount());
        reportingYear.setCurrentCost(reportingYear.getPlannedWorkCost());
        theModel.addAttribute("reportingYear", reportingYear);
        theModel.addAttribute("original", false);
        return "years/updatePlans";
    }

    @PostMapping ("years/doUpdatePlans")
    public ModelAndView updateReportingYearPlansByUser(ModelMap theModel, @ModelAttribute("reportingYear") @Valid ReportingYear reportingYear, BindingResult bindingResult) {

        String currentWorkAmount = reportingYear.getCurrentWork();
        String plannedWorkAmount = reportingYear.getPlannedWorkAmount();
        String currentWorkCost = reportingYear.getCurrentCost();
        String plannedWorkCost = reportingYear.getPlannedWorkCost();
        String comment = reportingYear.getCommentary();

        Boolean original = false;
        theModel.addAttribute("original", original);
        theModel.addAttribute("actionId", reportingYear.getAction().getId());

        try {
            String workError = reportingYearService.workPlansChanged(currentWorkAmount, plannedWorkAmount, comment);
            if (!workError.isEmpty()) {
                ObjectError workPlansDiffer = new ObjectError("workError", workError);
                bindingResult.addError(workPlansDiffer);
            }
            String costError = reportingYearService.costPlansChanged(currentWorkCost, plannedWorkCost, comment);
            if (!costError.isEmpty()) {
                ObjectError costPlansDiffer = new ObjectError("costError", costError);
                bindingResult.addError(costPlansDiffer);
            }

        } catch (NumberFormatException | NullPointerException e) {
            String noDigitsError = "отсутствуют цифры предполагаемого плана работ и/или стоимости";
            ObjectError noDigits = new ObjectError("noDigitsError", noDigitsError);
            bindingResult.addError(noDigits);
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView( "years/updatePlans");
        }
        reportingYear.setLastUpdate(TimeNameSetter.setTimeName());
        reportingYearService.saveReportingYear(reportingYear);

        return new ModelAndView("redirect:/years", theModel);


    }





}
