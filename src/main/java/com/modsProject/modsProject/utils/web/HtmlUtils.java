package com.modsProject.modsProject.utils.web;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

@Service
public class HtmlUtils {
    public static void loadHeaderParams(Model model, Principal principal, CreatorRepository creatorRepository) {
        if (principal != null && !principal.getName().isEmpty()) {
            long id = Long.parseLong(principal.getName());

            if (id > 0) {
                Optional<Creator> raw_creator = creatorRepository.findById(id);

                if (raw_creator.isPresent()) {
                    Creator creator = raw_creator.get();

                    model.addAttribute("auth_id", id);
                    model.addAttribute("auth_name", creator.getName());
                    model.addAttribute("auth_image", creator.getImagePath());
                    model.addAttribute("auth_status", true);
                    model.addAttribute("auth_success", true);

                    return;
                }
            }
        }

        model.addAttribute("auth_id", -1);
        model.addAttribute("auth_name", "Анонiм");
        model.addAttribute("auth_image", "anonymous.png");
        model.addAttribute("auth_status", true);
        model.addAttribute("auth_success", false);
    }

    public static void loadCreatorParams(Model model, Principal principal, Long id) {
        model.addAttribute("is_own", principal != null && principal.getName().equals(id.toString()));
    }
}
