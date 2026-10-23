package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetSystemDesignCatalogUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** The system design catalog. Access is enforced by the security filter chain, not here. */
@Controller
public class SystemDesignCatalogController {

    private final GetSystemDesignCatalogUseCase getSystemDesignCatalogUseCase;

    public SystemDesignCatalogController(GetSystemDesignCatalogUseCase getSystemDesignCatalogUseCase) {
        this.getSystemDesignCatalogUseCase = getSystemDesignCatalogUseCase;
    }

    @GetMapping("/sdc")
    public String catalog(Model model) {
        model.addAttribute("systemDesigns", getSystemDesignCatalogUseCase.getSystemDesigns());
        return "sdc";
    }
}
