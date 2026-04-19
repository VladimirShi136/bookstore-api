package com.example.bookstore_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер аутентификации
 *
 * @author vladimir_shi
 * @since 18.04.2026
 */
@Controller
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

