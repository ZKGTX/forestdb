package com.zerokikr.forestdb.controller;


import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.service.ActionService;
import com.zerokikr.forestdb.service.MeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ActionController {

    private ActionService actionService;

    private MeasureService measureService;

    public ActionController(ActionService actionService, MeasureService measureService) {
        this.actionService = actionService;
        this.measureService = measureService;
    }

    @GetMapping("/actions")
    public String showAllActionsByMeasureId(Model theModel, @RequestParam("measureId") Long measureId) {
        List<Action> allActions = actionService.getAllActionsByMeasureId(measureId);
        Measure measure = measureService.getMeasureById(measureId);
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
    public ModelAndView saveAction(ModelMap theModel, @ModelAttribute("action") Action action) {
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
