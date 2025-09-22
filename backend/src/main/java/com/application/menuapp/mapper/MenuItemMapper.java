package com.application.menuapp.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.application.menuapp.dto.MenuItemCreateUpdateDto;
import com.application.menuapp.dto.MenuItemDto;
import com.application.menuapp.entity.MenuItem;

@Component
@RequiredArgsConstructor
public class MenuItemMapper {

    private final ModelMapper mm;

    public MenuItemDto toDto(MenuItem entity) {
        return mm.map(entity, MenuItemDto.class);
    }

    public MenuItem toEntity(MenuItemCreateUpdateDto dto) {
        return mm.map(dto, MenuItem.class);
    }

    // Partial update: null olan field’ları es geçer (setSkipNullEnabled(true) sayesinde)
    public void updateEntity(MenuItemCreateUpdateDto dto, MenuItem target) {
        mm.map(dto, target);
    }
}
