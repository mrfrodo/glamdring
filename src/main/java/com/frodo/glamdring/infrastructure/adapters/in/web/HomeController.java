package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetKillLogUseCase;
import com.frodo.glamdring.application.ports.in.GetTechUseCase;
import com.frodo.glamdring.application.ports.in.GetTipOfTheDayUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final int INITIAL_TREND_LIMIT = 5;

    private final GetTechUseCase getTechUseCase;
    private final GetTipOfTheDayUseCase getTipOfTheDayUseCase;
    private final GetKillLogUseCase getKillLogUseCase;

    public HomeController(GetTechUseCase getTechUseCase, GetTipOfTheDayUseCase getTipOfTheDayUseCase, GetKillLogUseCase getKillLogUseCase) {
        this.getTechUseCase = getTechUseCase;
        this.getTipOfTheDayUseCase = getTipOfTheDayUseCase;
        this.getKillLogUseCase = getKillLogUseCase;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("trends", getTechUseCase.getTopTrends(INITIAL_TREND_LIMIT));
        model.addAttribute("tipOfTheDay", getTipOfTheDayUseCase.getTip().orElse(null));
        model.addAttribute("killCount", getKillLogUseCase.getKills().size());
        return "index";
    }
}
