package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignupController {

    @RequestMapping("/signup")
    public String showSignupPage(Model theModel) {
        User user = new User();
        theModel.addAttribute("user", user);
        return "security/signupPage";
    }

}
