package com.kdg.hexa_delivery.domain.shopping_basket.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class ShoppingBasketResponseDto {

    private final String storeName;

    private final Map<String, Integer> menuList;

    public ShoppingBasketResponseDto(String storeName, Map<String, Integer> menuList) {
        this.storeName = storeName;
        this.menuList = menuList;
    }
}
