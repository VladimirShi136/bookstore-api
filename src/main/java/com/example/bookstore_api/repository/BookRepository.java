package com.example.bookstore_api.repository;

import com.example.bookstore_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий книг
 *
 * @author vladimir_shi
 * @since 18.04.2026
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}