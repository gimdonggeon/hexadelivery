package com.kdg.hexa_delivery.domain.menu;

import lombok.Getter;

@Getter
public class updateMenuRequestDto {

    private final String menuName;

    private final Integer price;

    public updateMenuRequestDto(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }
}
