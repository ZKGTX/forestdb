package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.service.MeasureService;
import com.zerokikr.forestdb.service.RiskService;
import com.zerokikr.forestdb.utility.TimeNameSetter;
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
public class MeasureController {

    private MeasureService measureService;

    private RiskService riskService;

    public MeasureController(MeasureService measureService, RiskService riskService) {
        this.measureService = measureService;
        this.riskService = riskService;
    }

    @InitBinder
    public void initBinder (WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/measures")
    public String showAllMeasuresByRiskId(Model theModel, @RequestParam("riskId") Long riskId, @RequestParam(value = "keyword", required = false) String keyword) {
//        Specification<Measure> spec = Specification.where(MeasureSpecs.riskIdEqualsTo(riskId));
        Risk risk = riskService.getRiskById(riskId);
//        if (keyword != null) {
//            String theKeyword = keyword.toLowerCase();
//            spec = spec.and(MeasureSpecs.nameContains(theKeyword));
//        }

        List<Measure> measures = measureService.getAllMeasuresByRiskId(riskId);
        List<Measure> allMeasures = measureService.sortMeasuresAccordingToForestPlan(risk, measures);
        theModel.addAttribute("allMeasures", allMeasures);
        theModel.addAttribute("risk", risk);
        theModel.addAttribute("keyword", keyword);
        return "measures/measures";
    }

    @GetMapping("/measures/add")
    public String showAddForm (Model theModel, @RequestParam("riskId") Long riskId) {
        Measure measure = new Measure();
        Risk risk = riskService.getRiskById(riskId);
        measure.setRisk(risk);
        theModel.addAttribute("measure", measure);
        return "measures/addMeasure";
    }

    @GetMapping("/measures/update")
    public String showUpdateForm(@RequestParam("measureId") Long id, Model theModel) {
        Measure measure = measureService.getMeasureById(id);
        theModel.addAttribute("measure", measure);
        return "measures/addMeasure";
    }

    @GetMapping("measures/delete")
    public ModelAndView delete(ModelMap theModel, @RequestParam("measureId") Long measureId, @RequestParam("riskId") Long riskId) {
        measureService.deleteMeasureById(measureId);
        theModel.addAttribute("riskId", riskId);
        return new ModelAndView("redirect:/measures", theModel);
    }

    @PostMapping("/measures/save")
    public ModelAndView saveMeasure(ModelMap theModel, @ModelAttribute("measure") @Valid Measure measure, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView( "measures/addMeasure");
        }
        measure.setLastUpdate(TimeNameSetter.setTimeName());
        measureService.saveMeasure(measure);
        theModel.addAttribute("riskId", measure.getRisk().getId());
        return new ModelAndView("redirect:/measures", theModel);
    }

}
