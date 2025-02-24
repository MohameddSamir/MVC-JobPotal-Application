package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.Users;
import com.jobportal.jobportal.services.UsersService;
import com.jobportal.jobportal.services.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final UsersTypeService usersTypeService;
    @Autowired
    public UsersController(UsersTypeService usersTypeService, UsersService usersService){
        this.usersTypeService=usersTypeService;
        this.usersService=usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new Users());
        model.addAttribute("getAllTypes", usersTypeService.getAllUsersType());
        return "register";
    }
    @PostMapping("/register/new")
    public String addNew(@ModelAttribute("user") Users users, Model model){
        Users foundUser= usersService.getUserByEmail(users.getEmail());
        if(foundUser != null){
            model.addAttribute("error","This Email Already Registered, Please Try Another Email");
            model.addAttribute("user", new Users());
            model.addAttribute("getAllTypes", usersTypeService.getAllUsersType());
            return "register";
        }
        usersService.save(users);
        return "redirect:/dashboard/";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }
}
