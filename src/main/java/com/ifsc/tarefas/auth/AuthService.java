package com.ifsc.tarefas.auth;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "redirect", required = false) String redirect,
                            Model model) {
        model.addAttribute("redirect", redirect);
        return "login"; 
    }

    @GetMapping("/register")
    public String registerPage() {
        return "cadastro"; 
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam(name = "redirect", required = false) String redirect,
                          Model model,
                          HttpServletResponse response) {

        Optional<String> maybeToken = authRepository.login(username, password);
        if (maybeToken.isEmpty()) {
            model.addAttribute("erro", "Usuário ou senha inválidos");
            model.addAttribute("redirect", redirect);
            return "login";
        }

        String token = maybeToken.get();

        // Seta cookie httpOnly com o token para ser lido pelo filtro
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // cookie.setSecure(true); // ative em produção (HTTPS)
        response.addCookie(cookie);

        String target = (redirect == null || redirect.isBlank()) ? "/templates" : redirect;
        // Segurança básica: só permite redirects relativos dentro da aplicação
        if (target.contains("://")) {
            target = "/templates";
        }
        if (!target.startsWith("/")) {
            target = "/" + target;
        }
        // Não aplicar URLEncoder no caminho do redirect para não quebrar as barras
        return "redirect:" + target;
    }

    @PostMapping("/logout")
    public String logout(@RequestParam(name = "redirect", required = false) String redirect,
                         @RequestParam(name = "token", required = false) String tokenFromForm,
                         HttpServletResponse response) {

        // Tentativa de invalidar via token enviado pelo form (fallback)
        if (tokenFromForm != null && !tokenFromForm.isBlank()) {
            authRepository.logout(tokenFromForm);
        }

        // Remove cookie do cliente
        Cookie cookie = new Cookie("AUTH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        String target = (redirect == null || redirect.isBlank()) ? "/login" : redirect;
        return "redirect:" + target;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("erro", "Preencha usuário e senha");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("erro", "As senhas não coincidem");
            return "register";
        }
        boolean created = authRepository.register(username, password);
        if (!created) {
            model.addAttribute("erro", "Usuário já existe");
            return "register";
        }
        model.addAttribute("sucesso", "Usuário criado! Faça login.");
        return "login";
    }
}