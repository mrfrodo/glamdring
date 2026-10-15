package com.frodo.glamdring.infrastructure.adapters.in.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Exposes whether the visitor is logged in, so the header can show Log in or Log out. */
@ControllerAdvice
public class AuthenticationModelAdvice {

    @ModelAttribute("loggedIn")
    public boolean loggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
