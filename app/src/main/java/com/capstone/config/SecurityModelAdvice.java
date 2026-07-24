package com.capstone.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class SecurityModelAdvice {

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(Principal principal) {
        return principal != null;
    }
}
