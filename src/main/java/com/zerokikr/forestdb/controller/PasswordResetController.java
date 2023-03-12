package com.zerokikr.forestdb.controller;


import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.entity.User;
import com.zerokikr.forestdb.exception.UserNotFoundException;
import com.zerokikr.forestdb.service.UserService;
import net.bytebuddy.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "security/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String siteURL = request.getRequestURL().toString();
            String resetPasswordLink = siteURL.replace(request.getServletPath(), "") + "/resetPassword?token=" + token;
            userService.sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "На электронный адрес " + email + " отправлено письмо для сброса пароля");

        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Что-то пошло не так. Письмо не отправлено.");
        }

        return "security/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public ModelAndView showResetPasswordForm(Model model, @RequestParam(value = "token") String token, RedirectAttributes redirectAttributes) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("user", user);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Неверный, устаревший или использованный токен. Пароль не изменен.");
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("security/resetPassword");
    }

    @PostMapping("/resetPassword")
    public ModelAndView processResetPassword(ModelMap theModel, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String passwordError = userService.passwordsMatch(user);

        if (!passwordError.isEmpty()) {
            ObjectError noMatch = new ObjectError("passwordError", passwordError);
            bindingResult.addError(noMatch);
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("security/resetPassword");
        }

        user.setResetPasswordToken("");
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Вы успешно сменили пароль! Используйте адрес электронной почты и новый пароль для входа в систему.");
        return new ModelAndView("redirect:/login");

    }

    }

//    @PostMapping("/forgotPassword")
//    public String processForgotPassword() {
//
//    }




//    @GetMapping("/resetPassword")
//    public String showResetPasswordForm() {
//
//    }

//    @PostMapping("/resetPpassword")
//    public String processResetPassword() {
//
//    }


