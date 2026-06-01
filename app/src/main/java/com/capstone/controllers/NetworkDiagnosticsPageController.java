package com.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NetworkDiagnosticsPageController {

    @GetMapping("/network")
    public String networkDiagnosticsPage() {
        return "network-diagnostics";
    }
}
