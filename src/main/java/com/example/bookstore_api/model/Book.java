package com.example.bookstore_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель книги
 *
 * @author vladimir_shi
 * @since 31.03.2026
 */
@Entity
@Table(name = "books")
@Data // lombok аннотация для генерации getter, setter, toString, equals и hashCode
@NoArgsConstructor // Конструктор без аргументов
@AllArgsConstructor // Конструктор с аргументами
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // айди
    private String title; // название
    private String author; // автор
    private Integer publicationYear; // год выпуска
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; // владелец книги
}
