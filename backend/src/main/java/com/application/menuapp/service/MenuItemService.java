package com.application.menuapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.menuapp.dto.CategoryDTO;
import com.application.menuapp.dto.MenuItemDto;
import com.application.menuapp.entity.CategoryTranslation;
import com.application.menuapp.entity.MenuItem;
import com.application.menuapp.entity.MenuItemTranslation;
import com.application.menuapp.repository.CategoryRepository;
import com.application.menuapp.repository.CategoryTranslationRepository;
import com.application.menuapp.repository.MenuItemRepository;
import com.application.menuapp.repository.MenuItemTranslationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository repository;
    private final MenuItemTranslationRepository menuItemTranslationRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;

    
    public MenuItemDto create(MenuItemDto dto, String lang) {
        MenuItem item = new MenuItem();
        item.setImagePath(dto.getImagePath());
        item.setPrice(dto.getPrice());
        item.setCategory(
                categoryRepository.findById(dto.getCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı"))
        );

        MenuItem saved = repository.save(item);

        MenuItemTranslation translation = new MenuItemTranslation();
        translation.setMenuItem(saved);
        translation.setLanguageCode(lang);
        translation.setName(dto.getName());
        translation.setDescription(dto.getDescription());

        menuItemTranslationRepository.save(translation);

        String categoryName = categoryTranslationRepository
                .findByCategoryIdAndLanguageCode(saved.getCategory().getId(), lang)
                .map(CategoryTranslation::getName)
                .orElse("");

        return new MenuItemDto(
                saved.getId(),
                translation.getName(),
                translation.getDescription(),
                saved.getImagePath(),
                saved.getPrice(),
                new CategoryDTO(saved.getCategory().getId(), categoryName)
        );
    }

   
    public Optional<MenuItemDto> update(Long id, MenuItemDto dto, String lang) {
        return repository.findById(id).map(existing -> {
            existing.setImagePath(dto.getImagePath());
            existing.setPrice(dto.getPrice());
            existing.setCategory(
                    categoryRepository.findById(dto.getCategory().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı"))
            );

            MenuItem saved = repository.save(existing);

            
            MenuItemTranslation translation = menuItemTranslationRepository
                    .findByMenuItemIdAndLanguageCode(saved.getId(), lang)
                    .orElseGet(() -> {
                        MenuItemTranslation newTr = new MenuItemTranslation();
                        newTr.setMenuItem(saved);
                        newTr.setLanguageCode(lang);
                        return newTr;
                    });

            translation.setName(dto.getName());
            translation.setDescription(dto.getDescription());
            menuItemTranslationRepository.save(translation);

            String categoryName = categoryTranslationRepository
                    .findByCategoryIdAndLanguageCode(saved.getCategory().getId(), lang)
                    .map(CategoryTranslation::getName)
                    .orElse("");

            return new MenuItemDto(
                    saved.getId(),
                    translation.getName(),
                    translation.getDescription(),
                    saved.getImagePath(),
                    saved.getPrice(),
                    new CategoryDTO(saved.getCategory().getId(), categoryName)
            );
        });
    }

  
    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

   
    @Transactional(readOnly = true)
    public List<MenuItemDto> getMenuByLanguage(String lang) {
        return repository.findAll().stream()
                .map(item -> toDtoWithTranslation(item, lang))
                .toList();
    }

    
    @Transactional(readOnly = true)
    public MenuItemDto toDtoWithTranslation(MenuItem item, String lang) {
        // Menü item çevirisi
        MenuItemTranslation translation = menuItemTranslationRepository
                .findByMenuItemIdAndLanguageCode(item.getId(), lang)
                .orElseGet(() -> {
                    if (!"tr".equalsIgnoreCase(lang)) {
                        return menuItemTranslationRepository
                                .findByMenuItemIdAndLanguageCode(item.getId(), "tr")
                                .orElse(null);
                    }
                    return null;
                });

        String name = translation != null ? translation.getName() : "";
        String description = translation != null ? translation.getDescription() : "";

       
        String categoryName = categoryTranslationRepository
                .findByCategoryIdAndLanguageCode(item.getCategory().getId(), lang)
                .map(CategoryTranslation::getName)
                .orElseGet(() -> categoryTranslationRepository
                        .findByCategoryIdAndLanguageCode(item.getCategory().getId(), "tr")
                        .map(CategoryTranslation::getName)
                        .orElse("")
                );

        return new MenuItemDto(
                item.getId(),
                name,
                description,
                item.getImagePath(),
                item.getPrice(),
                new CategoryDTO(item.getCategory().getId(), categoryName)
        );
    }

    
    @Transactional(readOnly = true)
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id);
    }
}
