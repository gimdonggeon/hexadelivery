package com.kdg.hexa_delivery.domain.menu.dto;

import lombok.Getter;

@Getter
public class UpdateMenuRequestDto {

    private final String menuName;

    private final Integer price;

    private final String description;

    public UpdateMenuRequestDto(String menuName, Integer price, String description) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
    }
}
