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

    // ✅ Belirli dilde tüm kategorileri getir
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByLanguage(String lang) {
        List<CategoryTranslation> translations = categoryTranslationRepository.findByLanguageCode(lang);

        return translations.stream()
                .map(t -> new CategoryDTO(t.getCategory().getId(), t.getName()))
                .toList();
    }

    // ✅ Yeni kategori oluştur (dil parametresine göre translation ekle)
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto, String lang) {
        // 1. Yeni Category kaydet
        Category category = new Category();
        Category savedCategory = categoryRepository.save(category);

        // 2. Translation kaydet
        CategoryTranslation translation = new CategoryTranslation();
        translation.setCategory(savedCategory);
        translation.setLanguageCode(lang);
        translation.setName(dto.getName());
        categoryTranslationRepository.save(translation);

        // 3. DTO dön
        return new CategoryDTO(savedCategory.getId(), translation.getName());
    }

    // ✅ Kategoriye yeni dilde translation ekle/güncelle
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

    // ✅ Kategori silme
    @Transactional
    public void delete(Long categoryId) {
        // önce translation'ları sil
        categoryTranslationRepository.deleteById(categoryId);

        // sonra kategoriyi sil
        categoryRepository.deleteById(categoryId);
    }
}
