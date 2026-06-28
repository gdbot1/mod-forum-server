package com.modsProject.modsProject.server.controllers.creators;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.models.Mod;
import com.modsProject.modsProject.server.database.models.Source;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
import com.modsProject.modsProject.server.database.repositories.ModRepository;
import com.modsProject.modsProject.server.database.repositories.SourceRepository;
import com.modsProject.modsProject.utils.pages.PageUtils;
import com.modsProject.modsProject.utils.web.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CreatorModsController {
    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    ModRepository modRepository;

    public static final int page_size = 4;
    public static final int buttons_count = 7;

    @GetMapping("/creator/{id}/mods")
    public String getCreatorModsRedirect() {
        return "redirect:/creator/{id}/mods/page=0&search=";
    }

    @GetMapping("/creator/{id}/mods/page={page}&search={search}")
    public String getCreatorMods(@PathVariable(value = "id") Long id, @PathVariable(value = "page") int page, @PathVariable(value = "search") String search, Model model, Principal principal) {
        HtmlUtils.loadHeaderParams(model, principal, creatorRepository);
        HtmlUtils.loadCreatorParams(model, principal, id);

        Optional<Creator> creator = creatorRepository.findById(id);

        if (creator.isPresent()) {
            model.addAttribute("creator", creator.get());
        }
        else {
            model.addAttribute("message", "Error 404: Not Found");
            model.addAttribute("description", "Creator with id " + id + " is not found.");

            return "statuses/status";
        }

        Pageable pageable = PageRequest.of(page, page_size);
        Page<Mod> page_mods;

        if (search.isEmpty()) {
            page_mods = modRepository.findByCreatorId(id, pageable);
        }
        else {
            page_mods = modRepository.findByCreatorIdAndSimilarName(id, search, pageable);
        }

        List<Mod> mods = page_mods.getContent();

        int pages_count = page_mods.getTotalPages();

        if (pages_count != 0 && page >= pages_count || page < 0) {
            model.addAttribute("title", "400 Bad Request");
            model.addAttribute("message", "Error 400: Bad Request");
            model.addAttribute("description", "Page " + page + " is out of the bounds. Page: " + page + ", bounds: [0; " + pages_count + ")");

            return "statuses/status";
        }

        int[] pages = PageUtils.getPages(page, buttons_count, pages_count);

        List<Source> sources = sourceRepository.findByCreatorId(id);

        model.addAttribute("sources", sources);
        model.addAttribute("mods", mods);

        model.addAttribute("search", search);

        model.addAttribute("page", page);
        model.addAttribute("page_size", page_size);
        model.addAttribute("pages_count", pages_count);
        model.addAttribute("pages", pages);

        return "creatorMods";
    }
}
