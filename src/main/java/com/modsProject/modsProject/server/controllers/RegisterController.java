package com.modsProject.modsProject.server.controllers;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.dto.CreatorDto;
import com.modsProject.modsProject.server.security.services.AuthService;
import com.modsProject.modsProject.server.security.services.CreatorService;
import com.modsProject.modsProject.utils.FileUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class RegisterController {

    @Autowired
    AuthService authService;

    @Autowired
    CreatorService creatorService;

    @GetMapping("/register")
    public String getRegister(@RequestParam(value = "file", required = false) String file, @RequestParam(value = "avatar", required = false) String avatar, @RequestParam(value = "error", required = false) String error, Model model) {
        if (file != null) {
            model.addAttribute("error_message", "Расширения: .png .jpg .jpeg и .gif");
        }

        if (error != null) {
            model.addAttribute("error_message", "Данное имя пользователя уже занято");
        }

        if (avatar != null) {
            model.addAttribute("error_message", "Неизвестная ошибка сохранения аватара.");
        }

        model.addAttribute("error_status", error!=null || avatar!=null || file!=null);


        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("description") String description, @RequestParam("avatar") MultipartFile avatar, HttpServletResponse response) {
        String fileName = "anonymous.png";

        if (!avatar.isEmpty()) {
            String originalFileName = avatar.getOriginalFilename();

            String extension = FileUtils.extractExtension(originalFileName);

            if (!FileUtils.extensionIsImage(extension)) {
                return "redirect:/register?file";
            }

            fileName = "image_" + System.currentTimeMillis() + extension;

            Path path = Paths.get(FileUtils.uploadDirectory + fileName);

            try {
                FileUtils.saveMultipartFile(path, avatar);
            } catch (IOException e) {
                return "redirect:/register?avatar";
            }
        }

        CreatorDto creatorDto = new CreatorDto(username, password, description, fileName);

        try {
            creatorService.registration(creatorDto);
        }
        catch (DataIntegrityViolationException e) {
            return "redirect:/register?error";
        }

        //Успешная регистрация

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
        }
        catch (AuthorizationDeniedException e) {
            return "redirect:/login?error";
        }

        return "redirect:/";
    }
}
