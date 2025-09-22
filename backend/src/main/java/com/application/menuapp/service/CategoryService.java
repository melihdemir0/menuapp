package com.application.menuapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.menuapp.dto.CategoryDTO;
import com.application.menuapp.entity.Category;
import com.application.menuapp.entity.CategoryTranslation;
import com.application.menuapp.repository.CategoryRepository;
import com.application.menuapp.repository.CategoryTranslationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;

    
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByLanguage(String lang) {
        List<CategoryTranslation> translations = categoryTranslationRepository.findByLanguageCode(lang);

        return translations.stream()
                .map(t -> new CategoryDTO(t.getCategory().getId(), t.getName()))
                .toList();
    }

    
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto, String lang) {
        
        Category category = new Category();
        Category savedCategory = categoryRepository.save(category);

        
        CategoryTranslation translation = new CategoryTranslation();
        translation.setCategory(savedCategory);
        translation.setLanguageCode(lang);
        translation.setName(dto.getName());
        categoryTranslationRepository.save(translation);

        
        return new CategoryDTO(savedCategory.getId(), translation.getName());
    }

    
    @Transactional
    public CategoryDTO upsertCategoryTranslation(Long categoryId, CategoryDTO dto, String lang) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + categoryId));

        CategoryTranslation translation = categoryTranslationRepository
                .findByCategoryIdAndLanguageCode(categoryId, lang)
                .orElseGet(() -> {
                    CategoryTranslation newTr = new CategoryTranslation();
                    newTr.setCategory(category);
                    newTr.setLanguageCode(lang);
                    return newTr;
                });

        translation.setName(dto.getName());
        categoryTranslationRepository.save(translation);

        return new CategoryDTO(category.getId(), translation.getName());
    }

    
    @Transactional
    public void delete(Long categoryId) {
        
        categoryTranslationRepository.deleteById(categoryId);

        
        categoryRepository.deleteById(categoryId);
    }
}
