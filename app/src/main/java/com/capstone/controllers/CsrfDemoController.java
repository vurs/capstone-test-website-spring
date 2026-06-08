package com.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CsrfDemoController {

    @GetMapping("/csrf-demo")
    public String csrfDemo(Model model) {
        model.addAttribute("profileEmail", "testuser@example.com");
        model.addAttribute("displayName", "Test User");
        return "csrf-demo";
    }

    @PostMapping("/unsafe-forms/profile")
    public String updateProfile(@RequestParam String displayName, @RequestParam String email) {
        return "redirect:/csrf-demo?profileUpdated=true";
    }

    @PutMapping("/unsafe-forms/preferences")
    public String updatePreferences(@RequestParam String digestFrequency) {
        return "redirect:/csrf-demo?preferencesUpdated=true";
    }

    @DeleteMapping("/unsafe-forms/account")
    public String deleteAccount(@RequestParam String confirmation) {
        return "redirect:/csrf-demo?accountDeleted=true";
    }
}
