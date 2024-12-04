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

    public MenuRequestDto(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }
}
