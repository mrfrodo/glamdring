package com.frodo.glamdring.infrastructure.adapters.in.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** The secured area. Access is enforced by the security filter chain, not here. */
@Controller
public class MembersController {

    @GetMapping("/members")
    public String members(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "members";
    }
}
