package com.frodo.glamdring.infrastructure.adapters.in.web;

import com.frodo.glamdring.application.ports.in.GetSystemDesignCatalogUseCase;
import com.frodo.glamdring.domain.models.sd.SystemDesignId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

/** The system design catalog. Access is enforced by the security filter chain, not here. */
@Controller
public class SystemDesignCatalogController {

    private final GetSystemDesignCatalogUseCase getSystemDesignCatalogUseCase;

    public SystemDesignCatalogController(GetSystemDesignCatalogUseCase getSystemDesignCatalogUseCase) {
        this.getSystemDesignCatalogUseCase = getSystemDesignCatalogUseCase;
    }

    @GetMapping("/sdcs")
    public String catalog(Model model) {
        model.addAttribute("systemDesigns", getSystemDesignCatalogUseCase.getSystemDesigns());
        return "sdcs";
    }

    /** The path pattern only lets well-formed slugs through; anything else is a plain 404. */
    @GetMapping("/sdcs/{slug:[a-z0-9]+(?:-[a-z0-9]+)*}")
    public String systemDesign(@PathVariable String slug, Model model) {
        model.addAttribute("systemDesign", getSystemDesignCatalogUseCase.getSystemDesign(new SystemDesignId(slug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "sdc-design";
    }
}
