package com.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MaskingDemoController {

    @GetMapping("/masking-demo")
    public String maskingDemo() {
        return "masking-demo";
    }
}
