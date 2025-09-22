package com.application.menuapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.application.menuapp.dto.MenuItemDto;
import com.application.menuapp.service.MenuItemService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService service;

    
    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getAllByLang(
            @RequestParam(defaultValue = "tr") String lang) {
        return ResponseEntity.ok(service.getMenuByLanguage(lang));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "tr") String lang) {
        return service.findById(id)
                .map(item -> ResponseEntity.ok(service.toDtoWithTranslation(item, lang)))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Menu item bulunamadı"));
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MenuItemDto> create(
            @RequestBody MenuItemDto dto,
            @RequestParam(defaultValue = "tr") String lang) {
        try {
            MenuItemDto created = service.create(dto, lang);
            return ResponseEntity.created(URI.create("/menu/" + created.getId())).body(created);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, "Ürün oluşturulamadı: " + e.getMessage());
        }
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDto> update(
            @PathVariable Long id,
            @RequestBody MenuItemDto dto,
            @RequestParam(defaultValue = "tr") String lang) {
        return service.update(id, dto, lang)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Güncellenecek ürün bulunamadı"));
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.deleteById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Silinecek ürün bulunamadı");
        }
        return ResponseEntity.noContent().build();
    }
}
