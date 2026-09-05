package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetKillLogUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KillsController {

    private final GetKillLogUseCase getKillLogUseCase;

    public KillsController(GetKillLogUseCase getKillLogUseCase) {
        this.getKillLogUseCase = getKillLogUseCase;
    }

    @GetMapping("/kills")
    public String kills(Model model) {
        model.addAttribute("kills", getKillLogUseCase.getKills());
        return "kills";
    }
}
