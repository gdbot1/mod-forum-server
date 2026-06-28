package com.modsProject.modsProject.server.controllers.creators;

import com.modsProject.modsProject.server.database.models.Creator;
import com.modsProject.modsProject.server.database.repositories.CreatorRepository;
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
public class CreatorsCatalogController {
    @Autowired
    CreatorRepository creatorRepository;

    public static final int page_size = 6;
    public static final int buttons_count = 7;

    @GetMapping("/creators")
    public String getCreatorsRedirect() {
        return "redirect:/creators/page=0&search=";
    }

    @GetMapping("/creators/page={page}&search={search}")
    public String getCreatorsCatalog(@PathVariable(value = "page") int page, @PathVariable(value = "search") String search, Model model, Principal principal) {
        HtmlUtils.loadHeaderParams(model, principal, creatorRepository);

        Pageable pageable = PageRequest.of(page, page_size);
        Page<Creator> page_creators;

        if (search.isEmpty()) {
            page_creators = creatorRepository.findAll(pageable);
        }
        else {
            page_creators = creatorRepository.findBySimilarName(search, pageable);
        }

        List<Creator> creators = page_creators.getContent();

        int pages_count = page_creators.getTotalPages();

        if (page >= pages_count || page < 0) {
            model.addAttribute("title", "400 Bad Request");
            model.addAttribute("message", "Error 400: Bad Request");
            model.addAttribute("description", "Page " + page + " is out of the bounds. Page: " + page + ", bounds: [0; " + pages_count + ")");

            return "statuses/status";
        }

        int[] pages = PageUtils.getPages(page, buttons_count, pages_count);

        model.addAttribute("creators", creators);

        model.addAttribute("search", search);

        model.addAttribute("page", page);
        model.addAttribute("page_size", page_size);
        model.addAttribute("pages_count", pages_count);
        model.addAttribute("pages", pages);

        return "creatorsCatalog";
    }
}
