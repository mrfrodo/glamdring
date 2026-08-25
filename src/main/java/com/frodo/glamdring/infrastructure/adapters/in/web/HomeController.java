package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetTechTrendsUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final int INITIAL_TREND_LIMIT = 5;

    private final GetTechTrendsUseCase getTechTrendsUseCase;

    public HomeController(GetTechTrendsUseCase getTechTrendsUseCase) {
        this.getTechTrendsUseCase = getTechTrendsUseCase;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("trends", getTechTrendsUseCase.getTopTrends(INITIAL_TREND_LIMIT));
        return "index";
    }
}
