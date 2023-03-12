package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.entity.User;
import com.zerokikr.forestdb.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SubjectService subjectService;

    @RequestMapping("/signup")
    public String showSignupPage(Model theModel) {
        User user = new User();
        List<Subject> subjects = subjectService.getAllSubjects();
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add("");
        for (Subject s: subjects) {
            subjectNames.add(s.getName());
        }
        theModel.addAttribute("user", user);
        theModel.addAttribute("subjectNames", subjectNames);
        return "security/signupPage";
    }

}
