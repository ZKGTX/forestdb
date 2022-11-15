package com.zerokikr.forestdb.controller;


import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.repository.specification.ActionSpecs;
import com.zerokikr.forestdb.service.ActionService;
import com.zerokikr.forestdb.service.MeasureService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.jpa.domain.Specification;
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
public class ActionController {

    private ActionService actionService;

    private MeasureService measureService;

    public ActionController(ActionService actionService, MeasureService measureService) {
        this.actionService = actionService;
        this.measureService = measureService;
    }

    @InitBinder
    public void initBinder (WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/actions")
    public String showAllActionsByMeasureId(Model theModel, @RequestParam("measureId") Long measureId, @RequestParam(value = "keyword", required = false) String keyword) {
        Specification<Action> spec = Specification.where(ActionSpecs.measureIdEqualsTo(measureId));
        Measure measure = measureService.getMeasureById(measureId);
        if (keyword != null) {
            spec = spec.and(ActionSpecs.nameContains(keyword));
        }
        List<Action> allActions = actionService.getActionsByMeasureIdAndKeyword(spec);
        theModel.addAttribute("keyword", keyword);
        theModel.addAttribute("allActions", allActions);
        theModel.addAttribute("measure", measure);
        return "actions/actions";
    }

    @GetMapping("/actions/add")
    public String showAddForm (Model theModel, @RequestParam("measureId") Long measureId) {
        Action action = new Action();
        Measure measure = measureService.getMeasureById(measureId);
        action.setMeasure(measure);
        theModel.addAttribute("action", action);
        return "actions/addAction";
    }

    @PostMapping("/actions/save")
    public ModelAndView saveAction(ModelMap theModel, @ModelAttribute("action") @Valid Action action, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView( "actions/addAction");
        }
        actionService.saveAction(action);
        theModel.addAttribute("measureId", action.getMeasure().getId());
        return new ModelAndView("redirect:/actions", theModel);
    }

    @GetMapping("/actions/update")
    public String showUpdateForm(@RequestParam("actionId") Long id, Model theModel) {
        Action action = actionService.getActionById(id);
        theModel.addAttribute("action", action);
        return "actions/addAction";
    }

    @GetMapping("actions/delete")
    public ModelAndView delete(ModelMap theModel, @RequestParam("actionId") Long actionId, @RequestParam("measureId") Long measureId) {
        actionService.deleteActionById(actionId);
        theModel.addAttribute("measureId", measureId);
        return new ModelAndView("redirect:/actions", theModel);
    }










}
