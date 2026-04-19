package com.example.bookstore_api.controller;

import com.example.bookstore_api.model.Book;
import com.example.bookstore_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Контроллер для управления книгами
 *
 * @author vladimir_shi
 * @since 31.03.2026
 */
@RestController // Спринг аннотация для обозначения контроллера
@RequestMapping("/api/books")
@RequiredArgsConstructor //
public class BookController {
    private final BookRepository bookRepository;

    // Метод для получения списка всех книг
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')") // - разрешение всем ролям
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // -------- CRUD операции --------

    /*
    * === READ ===
    * @GetMapping используется для обработки HTTP GET запросов
    * Метод для получения книги по идентификатору
    * @PathVariable используется для извлечения значения из пути запроса
    * @RequestParam используется для извлечения значения из строки запроса
    */
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /*
    * === CREATE для admin и editor ===
    * @PreAuthorize используется для предоставления доступа
    * @PostMapping используется для обработки HTTP POST запросов
    * Post запрос это создание нового ресурса
    * @RequestBody связывает тело запроса с параметром метода
    * @RequestBody преобразует JSON тело запроса в объект Java
    */
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /*
    * === UPDATE для admin и editor ===
    * @PreAuthorize используется для предоставления доступа
    * @PutMapping используется для обработки HTTP PUT запросов
    * Put запрос это обновление существующего ресурса
    * @RequestBody связывает тело запроса с параметром метода
    * @RequestBody преобразует JSON тело запроса в объект Java
    */
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PutMapping
    public Book updateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /*
    * === DELETE только для admin ===
    * @DeleteMapping используется для обработки HTTP DELETE запросов
    * Delete запрос это удаление ресурса
    * @PathVariable используется для извлечения значения из пути запроса
    */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
