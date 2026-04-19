package com.example.bookstore_api.config;

import com.example.bookstore_api.model.User;
import com.example.bookstore_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Инициализация данных
 *
 * @author vladimir_shi
 * @since 18.04.2026
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "admin",
                    passwordEncoder.encode("admin123"), User.Role.ADMIN));
            userRepository.save(new User(null, "editor",
                    passwordEncoder.encode("editor123"), User.Role.EDITOR));
            userRepository.save(new User(null, "user",
                    passwordEncoder.encode("user123"), User.Role.USER));
        }
    }
}