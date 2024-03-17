package org.zad2.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.zad2.lookup.LookupQuery;
import org.zad2.lookup.LookupService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class LookupController {

    private final LookupService lookupService;

    @GetMapping
    public String redirect() {
        return "redirect:/v1/news";
    }

    @GetMapping("v1/news")
    public String homePage(Model model) {
        model.addAttribute("query", new LookupQuery());
        return "home";
    }

    @PostMapping("v1/news/search")
    public Mono<Rendering> lookupSubmit(@ModelAttribute LookupQuery query) {
        var lookupResult = lookupService.lookup(query);
        return Mono.just(Rendering.view("result")
                .modelAttribute("news", lookupResult)
                .modelAttribute("query", "Results for query %s".formatted(query.getSearch()))
                .build());
    }
}
