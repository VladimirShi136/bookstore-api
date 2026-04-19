package com.example.bookstore_api.controller;

import com.example.bookstore_api.model.Book;
import com.example.bookstore_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

/**
 * Контроллер веб страницы книг
 *
 * @author vladimir_shi
 * @since 01.04.2026
 */
@Controller
@RequiredArgsConstructor
public class BookWebController {
    private final BookRepository bookRepository;

    // Получить страницу с книгами (для всех)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("currentTime", new Date()); // Добавляем текущее время
        return "list";
    }

    // Получить страницу для добавления книги (редактор и админ)
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    // Добавить книгу (редактор и админ)
    // ModelAttribute связывает объект Book с данными формы
    // redirect:/books - перенаправление на страницу со списком книг после добавления
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    // Получить страницу для редактирования книги
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "edit";
    }

    // Обновить книгу
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        // Находим и заменяем книгу
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            bookRepository.deleteById(id);
            bookRepository.save(book);
        }
        return "redirect:/books";
    }

    // Удалить книгу
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}
