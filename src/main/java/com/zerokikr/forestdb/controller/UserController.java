package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Role;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.entity.User;
import com.zerokikr.forestdb.exception.UserExistsException;
import com.zerokikr.forestdb.repository.RoleRepository;
import com.zerokikr.forestdb.service.SubjectService;
import com.zerokikr.forestdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private SubjectService subjectService;
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/users")
    public String showAllUsers(Model theModel) {
        List<User> allUsers = userService.getAllUsers();
        theModel.addAttribute("allUsers", allUsers);
        return "security/users";
    }

    @GetMapping("/users/update")
    public String showUpdateUserForm(@RequestParam ("userId") long userId, Model theModel) {
        User user = userService.getUserById(userId);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        List <String> subjectNames = new ArrayList<>();
        subjectNames.add("");
        List<Subject> subjects = subjectService.getAllSubjects();
        for (Subject s: subjects) {
            subjectNames.add(s.getName());
        }
        theModel.addAttribute("subjectNames", subjectNames);
        theModel.addAttribute("user", user);
        theModel.addAttribute("roles", roles);
        return "security/updateUser";
    }

    @GetMapping("/users/delete")
    public String delete(@RequestParam("userId") Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/users";
    }

    @PostMapping("/users/save")
    public ModelAndView saveUser(ModelMap theModel, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String emailError = userService.emailExists(user.getEmail());


        if (!emailError.isEmpty()) {
            ObjectError sameEmail = new ObjectError("emailError", emailError);
            bindingResult.addError(sameEmail);
        }

        String passwordError = userService.passwordsMatch(user);

        if (!passwordError.isEmpty()) {
            ObjectError noMatch = new ObjectError("passwordError", passwordError);
            bindingResult.addError(noMatch);
        }

        if (bindingResult.hasErrors()) {
            List <String> subjectNames = new ArrayList<>();
            subjectNames.add("");
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject s: subjects) {
                subjectNames.add(s.getName());
            }
            theModel.addAttribute("subjectNames", subjectNames);
            return new ModelAndView("security/signupPage");
        }

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегистрировались! Используйте адрес электронной почты и пароль для входа в систему.");
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/users/doUpdate")
    public String updateTheUser(ModelMap theModel, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List <String> subjectNames = new ArrayList<>();
            subjectNames.add("");
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject s: subjects) {
                subjectNames.add(s.getName());
            }
            List<Role> roles = (List<Role>) roleRepository.findAll();
            theModel.addAttribute("roles", roles);
            theModel.addAttribute("subjectNames", subjectNames);

            return "security/updateUser";
        }
        userService.updateUser(user);
        return "redirect:/users";
    }
}
