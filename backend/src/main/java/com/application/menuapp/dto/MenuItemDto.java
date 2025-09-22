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
	private String name; 
	private String description; 
	private String imagePath;
	private BigDecimal price;
	private CategoryDTO category; 
}
