package com.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClickjackingDemoController {

    @GetMapping("/clickjacking-demo")
    public String clickjackingDemo() {
        // Intentionally frameable for the scanner's clickjacking detection.
        return "clickjacking-demo";
    }
}

