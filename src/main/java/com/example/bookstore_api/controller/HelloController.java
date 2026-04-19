package com.example.bookstore_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер приветствия
 *
 * @author vladimir_shi
 * @since 22.03.2026
 */
@RestController // Спринг аннотация для контроллера
public class HelloController {

    // Метод для получения приветствия через параметр name
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + " !";
    }

    /*
     Метод для получения приветствия через параметр запроса
     @RequestParam позволяет извлекать параметры запроса из URL
     value - имя параметра, defaultValue - значение по умолчанию
    */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }
}
