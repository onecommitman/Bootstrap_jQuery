package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@Controller
public class MainController {

    @GetMapping("admin")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("user")
    public String userPage() {
        return "user";
    }

}