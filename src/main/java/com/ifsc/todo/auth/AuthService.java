package com.ifsc.todo.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/authTemplate")
public class AuthService {
    private final AuthRepository authRepository;
    public AuthService(AuthRepository authRepository){
        this.authRepository = authRepository;
    }

    @GetMapping("/register")
    public String registerPage(){
        return "_register";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "_redirect", required = false) String redirect, Model model){
        model.addAttribute("_redirect", redirect);
        return "_login";
    }
}