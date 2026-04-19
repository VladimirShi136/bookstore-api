package com.example.bookstore_api.controller;

import com.example.bookstore_api.model.User;
import com.example.bookstore_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Контроллер регистрации
 *
 * @author vladimir_shi
 * @since 19.04.2026
 */
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Показать форму регистрации
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Обработать регистрацию нового пользователя
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Проверка, существует ли пользователь
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }

        // Установка роли USER по умолчанию и кодирование пароля
        user.setRole(User.Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохранение пользователя
        userRepository.save(user);

        // Редирект на страницу входа с сообщением об успехе
        return "redirect:/login?registered";
    }
}