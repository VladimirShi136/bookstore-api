package com.example.bookstore_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер стартовой страницы
 *
 * @author vladimir_shi
 * @since 22.03.2026
 */
@Controller
public class HelloController {

    /**
     * Стартовая страница с выбором действия
     */
    @GetMapping("/")
    public String home() {
        return "hello";
    }
}