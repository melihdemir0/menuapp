package com.application.menuapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/admin")
    public String adminHome() {
        return "admin/index"; // backend/templates/admin/index.html
    }
}
