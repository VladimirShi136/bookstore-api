package com.example.bookstore_api.repository;

import com.example.bookstore_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозитарий пользователей
 *
 * @author vladimir_shi
 * @since 18.04.2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
