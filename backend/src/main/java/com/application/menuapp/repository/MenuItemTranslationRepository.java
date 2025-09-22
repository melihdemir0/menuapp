package com.application.menuapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.menuapp.entity.MenuItemTranslation;

import java.util.List;
import java.util.Optional;

//MenuItemTranslationRepository.java
public interface MenuItemTranslationRepository extends JpaRepository<MenuItemTranslation, Long> {

 List<MenuItemTranslation> findByLanguageCode(String languageCode);

 Optional<MenuItemTranslation> findByMenuItemIdAndLanguageCode(Long menuItemId, String languageCode);
}

