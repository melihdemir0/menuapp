package com.application.menuapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.application.menuapp.dto.CategoryDTO;
import com.application.menuapp.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ Public: çoklu dil destekli listeleme
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestParam(defaultValue = "tr") String lang) {
        List<CategoryDTO> categories = categoryService.getCategoriesByLanguage(lang);
        return ResponseEntity.ok(categories);
    }

    // ✅ Admin: kategori oluşturma (dil parametresine göre translation ekler)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto,
                                              @RequestParam(defaultValue = "tr") String lang) {
        CategoryDTO created = categoryService.createCategory(dto, lang);
        return ResponseEntity.ok(created);
    }

    // ✅ Admin: var olan kategoriye farklı dilde translation ekle/güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> upsertTranslation(@PathVariable Long id,
                                                         @RequestBody CategoryDTO dto,
                                                         @RequestParam(defaultValue = "tr") String lang) {
        CategoryDTO updated = categoryService.upsertCategoryTranslation(id, dto, lang);
        return ResponseEntity.ok(updated);
    }

    // ✅ Admin: kategori silme
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
