package com.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/target-page")
    public String goToTargetPage() {
        return "target-page";
    }

//    @GetMapping("/")
//    public String goToIndex() {
//        return "index";
//    }

}
