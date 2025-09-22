package com.application.menuapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemCreateUpdateDto {

    @NotBlank private String nameTr;
    @NotBlank private String nameEn;
    @NotBlank private String nameDe;
    @NotBlank private String nameFr;

    private String descriptionTr;
    private String descriptionEn;
    private String descriptionDe;
    private String descriptionFr;

    @NotNull @Positive private Double price;
    private String category;
}
