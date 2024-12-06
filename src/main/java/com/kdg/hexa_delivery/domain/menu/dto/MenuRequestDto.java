package com.kdg.hexa_delivery.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuRequestDto {

    @NotBlank
    private final String menuName;

    @NotNull
    private final Integer price;

    @NotNull
    private final String description;

    public MenuRequestDto(String menuName, Integer price, String description) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
    }
}
