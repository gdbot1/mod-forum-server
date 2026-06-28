package com.modsProject.modsProject.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class MainController {
    @GetMapping("/")
    public String getMain() {
        return "redirect:/mods";
    }

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }

    @PostMapping("/test")
    public String postTest(@RequestParam("avatar") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("error 1");
            return "redirect:/test?error";
        }
        try {
            String uploadDirectory = "public/images/creators/";

            String fileName = file.getOriginalFilename();

            Path path = Paths.get(uploadDirectory + fileName);

            System.out.println("path: " + path.toAbsolutePath());

            file.transferTo(path);

            System.out.println("File are saved successfully");
        }
        catch (IOException e) {
            System.out.println("error 2");
            return "redirect:/test?error";
        }
        return "redirect:/";
    }
}
