package com.application.menuapp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
	private Long id;
	private String name; // seçili dilde isim
	private String description; // seçili dilde açıklama
	private String imagePath;
	private BigDecimal price;
	private CategoryDTO category; // DTO içinde kategori
}
