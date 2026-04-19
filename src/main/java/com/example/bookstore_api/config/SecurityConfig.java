package com.example.bookstore_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности
 *
 * @author vladimir_shi
 * @since 18.04.2026
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(auth -> auth
                        // Доступ для всех
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()

                        // Доступ для всех аутентифицированных пользователей
                        .requestMatchers("/books", "/books/add", "/books/edit/**", "/books/delete/**", "/api/books", "/api/books/**").hasAnyRole("USER", "EDITOR", "ADMIN")

                        // Доступ для редакторов и админов (API)
                        .requestMatchers("/api/books", "/api/books/**").hasAnyRole("EDITOR", "ADMIN")

                        // Доступ только для админов
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/books?login=true", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
