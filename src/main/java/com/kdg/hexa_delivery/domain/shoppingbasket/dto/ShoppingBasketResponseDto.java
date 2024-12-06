package com.kdg.hexa_delivery.domain.shoppingbasket.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ShoppingBasketResponseDto {

    private final String storeName;

    private final Map<String, Integer> menuList;

    private final List<String> imageUrls;

    public ShoppingBasketResponseDto(String storeName, Map<String, Integer> menuList, List<String> imageUrls) {
        this.storeName = storeName;
        this.menuList = menuList;
        this.imageUrls = imageUrls;
    }
}
