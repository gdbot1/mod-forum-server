package com.modsProject.modsProject.server.controllers.mods;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.models.Mod;
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
import java.util.*;

@Controller
public class ModsCatalogController {

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    ModRepository modRepository;

    @Autowired
    SourceRepository sourceRepository;

    public static final int page_size = 6;
    public static final int buttons_count = 7;

    @GetMapping("/mods")
    public String getModsRedirect() {
        return "redirect:/mods/page=0&search=&filter=";
    }

    @GetMapping("/mods/page={page}&search={search}&filter={filter}")
    public String getModsCatalog(@PathVariable(value = "page") int page, @PathVariable(value = "search") String search, @PathVariable(value = "filter") String filter, Model model, Principal principal) {
        HtmlUtils.loadHeaderParams(model, principal, creatorRepository);

        Pageable pageable = PageRequest.of(page, page_size);
        Page<Mod> page_mods;

        if (search.isEmpty()) {
            page_mods = modRepository.findAll(pageable);
        }
        else {
            switch (filter) {
                case "mod" -> page_mods = modRepository.findBySimilarName(search, pageable);
                case "creator" -> page_mods = modRepository.findByCreatorSimilarName(search, pageable);
                default -> page_mods = modRepository.findAll(pageable);
            }
        }

        List<Mod> mods = page_mods.getContent();

        Map<Long, Creator> map = new HashMap<>();

        int pages_count = page_mods.getTotalPages();

        if (page >= pages_count || page < 0) {
            model.addAttribute("title", "400 Bad Request");
            model.addAttribute("message", "Error 400: Bad Request");
            model.addAttribute("description", "Page " + page + " is out of the bounds. Page: " + page + ", bounds: [0; " + pages_count + ")");

            return "statuses/status";
        }

        for (Mod mod : mods) {
            Optional<Creator> creator = creatorRepository.findById(mod.getCreatorId());

            creator.ifPresent(value -> map.put(value.getId(), value));
        }

        int[] pages = PageUtils.getPages(page, buttons_count, pages_count);

        model.addAttribute("mods", mods);
        model.addAttribute("map", map);

        model.addAttribute("search", search);

        model.addAttribute("page", page);
        model.addAttribute("page_size", page_size);
        model.addAttribute("pages_count", pages_count);
        model.addAttribute("pages", pages);

        model.addAttribute("filter", filter);

        return "modsCatalog";
    }
}
