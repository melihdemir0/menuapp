package com.application.menuapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.menuapp.entity.CategoryTranslation;

import java.util.List;
import java.util.Optional;

//CategoryTranslationRepository.java
public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Long> {

 List<CategoryTranslation> findByLanguageCode(String languageCode);

 Optional<CategoryTranslation> findByCategoryIdAndLanguageCode(Long categoryId, String languageCode);
}

