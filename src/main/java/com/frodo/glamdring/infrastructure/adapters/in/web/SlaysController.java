package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetSlayLogUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SlaysController {

    private final GetSlayLogUseCase getSlayLogUseCase;

    public SlaysController(GetSlayLogUseCase getSlayLogUseCase) {
        this.getSlayLogUseCase = getSlayLogUseCase;
    }

    @GetMapping("/slays")
    public String slays(Model model) {
        model.addAttribute("slays", getSlayLogUseCase.getSlays());
        return "slays";
    }
}
