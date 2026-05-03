package com.capstone.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // Do not use this file as an actual controller. It is just a demonstration of how to secure a page behind a login and get the logged in user's details.

    @GetMapping("/secure")
    public String secure(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getPreferredUsername();
    }
}
