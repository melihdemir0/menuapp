package com.application.menuapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_item_translation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"menu_item_id", "language_code"}))
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MenuItemTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    @JsonBackReference // JSON serialization için
    private MenuItem menuItem;

    @Column(name = "language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
