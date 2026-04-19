package com.example.bookstore_api.controller;

import com.example.bookstore_api.model.Book;
import com.example.bookstore_api.model.User;
import com.example.bookstore_api.repository.BookRepository;
import com.example.bookstore_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Objects;

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
    private final UserRepository userRepository;

    // Получить страницу с книгами (для всех)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @GetMapping("/books")
    public String listBooks(Model model, @RequestParam(required = false) Boolean login) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("currentTime", new Date()); // Добавляем текущее время
        if (login != null && login) {
            model.addAttribute("successMessage", "✅ Вы успешно вошли в систему!");
        }
        return "list";
    }

    /**
     * Показать книги текущего пользователя
     */
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @GetMapping("/my-books")
    public String myBooks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("books", bookRepository.findByOwnerUsername(userDetails.getUsername()));
        model.addAttribute("currentTime", new Date());
        model.addAttribute("isMyBooks", true); // флаг для шаблона
        return "my-books";
    }

    // Получить страницу для добавления книги (редактор и админ)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    // Добавить книгу (редактор и админ)
    // ModelAttribute связывает объект Book с данными формы
    // redirect:/books - перенаправление на страницу со списком книг после добавления
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        book.setOwner(user);
        bookRepository.save(book);
        return "redirect:/books";
    }

    // Получить страницу для редактирования книги (владелец, редактор или админ)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/books";
        }
        // Проверка прав: владелец или EDITOR/ADMIN
        if (hasNoEditPermission(book.getOwner(), userDetails)) {
            return "redirect:/books?error=no_permission";
        }
        model.addAttribute("book", book);
        return "edit";
    }

    // Обновить книгу (владелец, редактор или админ)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book, @AuthenticationPrincipal UserDetails userDetails) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            return "redirect:/books";
        }
        // Проверка прав: владелец или EDITOR/ADMIN
        if (hasNoEditPermission(existingBook.getOwner(), userDetails)) {
            return "redirect:/books?error=no_permission";
        }
        book.setId(id);
        book.setOwner(existingBook.getOwner()); // Сохраняем владельца
        bookRepository.save(book);
        return "redirect:/books";
    }

    // Удалить книгу (владелец или админ)
    @PreAuthorize("hasAnyRole('USER', 'EDITOR', 'ADMIN')")
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/books";
        }
        // Проверка прав: владелец или ADMIN (EDITOR не может удалять)
        if (hasNoDeletePermission(book.getOwner(), userDetails)) {
            return "redirect:/books?error=no_permission";
        }
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    /**
     * Проверка: есть ли у пользователя право на редактирование книгу (владелец или EDITOR/ADMIN)
     * Возвращает true если прав НЕТ
     */
    private boolean hasNoEditPermission(User owner, UserDetails userDetails) {
        boolean isOwner = owner.getUsername().equals(userDetails.getUsername());
        boolean isEditorOrAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_EDITOR") || Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
        return !(isOwner || isEditorOrAdmin);
    }

    /**
     * Проверка: есть ли у пользователя право на удаление книги (владелец или ADMIN)
     * Возвращает true если прав НЕТ
     */
    private boolean hasNoDeletePermission(User owner, UserDetails userDetails) {
        boolean isOwner = owner.getUsername().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
        return !(isOwner || isAdmin);
    }
}
