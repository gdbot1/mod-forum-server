package com.modsProject.modsProject.server.controllers.creators;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CreatorController {
    @GetMapping("/creator/{id}")
    public String getCreatorRedirect(@PathVariable Long id) {
        return "redirect:/creator/{id}/mods";
    }
}
