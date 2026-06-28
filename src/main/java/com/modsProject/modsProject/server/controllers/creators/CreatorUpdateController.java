package com.modsProject.modsProject.server.controllers.creators;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import com.modsProject.modsProject.server.database.repositories.SourceRepository;
import com.modsProject.modsProject.utils.FileUtils;
import com.modsProject.modsProject.utils.web.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

@Controller
public class CreatorUpdateController {
    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    SourceRepository sourceRepository;

    @GetMapping("creator/{id}/update")
    public String getCreatorUpdate(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null || principal.getName().equals("-1") || !principal.getName().equals(id.toString())) {
            return "redirect:/creator/" + id;
        }

        HtmlUtils.loadHeaderParams(model, principal, creatorRepository);

        Optional<Creator> raw_creator = creatorRepository.findById(id);

        if (raw_creator.isEmpty()) {
            return "redirect:/creator/" + id;
        }

        Creator creator = raw_creator.get();

        model.addAttribute("creator", creator);

        model.addAttribute("id", id);

        return "creator/creatorUpdate";
    }

    @PostMapping("creator/{id}/update")
    public String postCreatorUpdate(@RequestParam("username") String username, @RequestParam("description") String description, @RequestParam("avatar") MultipartFile avatar, @PathVariable Long id, Model model, Principal principal) {
        if (principal == null || principal.getName().equals("-1") || !principal.getName().equals(id.toString())) {
            return "redirect:/creator/" + id;
        }

        Optional<Creator> raw_creator = creatorRepository.findById(id);

        if (raw_creator.isEmpty()) {
            return "redirect:/creator/" + id;
        }

        Creator creator = raw_creator.get();

        try {
            if (!avatar.isEmpty()) {
                String originalFileName = avatar.getOriginalFilename();

                String extension = FileUtils.extractExtension(originalFileName);

                if (!FileUtils.extensionIsImage(extension)) {
                    return "redirect:/creator/" + id + "/update?file";
                }

                if (creator.getImagePath().equals("anonymous.png")) {
                    String fileName = "image_" + System.currentTimeMillis() + extension;

                    Path path = Paths.get(FileUtils.uploadDirectory + fileName);

                    FileUtils.saveMultipartFile(path, avatar);
                    creatorRepository.updateImagePath(id, fileName);
                }
                else {
                    Path path = Paths.get(FileUtils.uploadDirectory + creator.getImagePath());

                    FileUtils.changeMultipartFile(path, avatar);
                }
            }
        } catch (IOException e) {
            return "redirect:/creator/" + id + "/update?avatar";
        }

        try {
            if (!username.equals(creator.getName()) || !description.equals(creator.getDescription())) {
                creatorRepository.updateNameAndDescription(id, username, description);
            }
        } catch (DataIntegrityViolationException e) {
            return "redirect:/creator/" + id + "/update?error";
        }

        return "redirect:/creator/" + id;
    }
}