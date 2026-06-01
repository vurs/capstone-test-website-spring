package com.capstone.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionProbeController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> sessionProbe(
            @AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> userDetails = new LinkedHashMap<>();
        userDetails.put("username", user.getPreferredUsername());
        userDetails.put("email", user.getEmail());
        userDetails.put("name", user.getFullName());
        userDetails.put("subject", user.getSubject());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("authenticated", true);
        body.put("user", userDetails);

        return ResponseEntity.ok(body);
    }
}
