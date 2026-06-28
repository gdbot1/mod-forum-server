package com.modsProject.modsProject.server.controllers;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.security.services.AuthService;
import com.modsProject.modsProject.server.security.services.CreatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    AuthService authService;

    @Autowired
    CreatorService creatorService;

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "expired", required = false) String expired, @RequestParam(value = "error", required = false) String error, Model model) {
        if (expired != null) {
            model.addAttribute("error_message", "Время сессии истекло. Пожалуйста, войдите снова.");
        }

        if (error != null) {
            model.addAttribute("error_message", "Неверное имя пользователя или пароль.");
        }

        model.addAttribute("error_status", error!=null || expired!=null);

        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        try {
            Creator creator = creatorService.authenticate(username, password);

            String jwtToken = authService.generateJwtToken(creator.getId());
            String sessionKey = authService.generateSessionKey(creator.getId());

            Cookie jwtCookie = new Cookie("jwt_token", jwtToken);
            jwtCookie.setMaxAge(7 * 24 * 60 * 60);
            jwtCookie.setHttpOnly(false);
            jwtCookie.setPath("/");

            Cookie sessionCookie = new Cookie("session_key", sessionKey);
            sessionCookie.setMaxAge(7 * 24 * 60 * 60);
            sessionCookie.setHttpOnly(false);
            sessionCookie.setPath("/");

            response.addCookie(jwtCookie);
            response.addCookie(sessionCookie);

            return "redirect:/";
        }
        catch (AuthorizationDeniedException e) {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/login/anonymous")
    public String GetLoginAnonymous(HttpServletResponse response) {
        try {
            Cookie jwtCookie = new Cookie("jwt_token", "");
            jwtCookie.setMaxAge(7 * 24 * 60 * 60);
            jwtCookie.setHttpOnly(false);
            jwtCookie.setPath("/");

            Cookie sessionCookie = new Cookie("session_key", "");
            sessionCookie.setMaxAge(7 * 24 * 60 * 60);
            sessionCookie.setHttpOnly(false);
            sessionCookie.setPath("/");

            response.addCookie(jwtCookie);
            response.addCookie(sessionCookie);

            return "redirect:/";
        }
        catch (AuthorizationDeniedException e) {
            return "redirect:/login?error";
        }
    }
}
