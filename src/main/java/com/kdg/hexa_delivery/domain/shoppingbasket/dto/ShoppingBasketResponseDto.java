package com.kdg.hexa_delivery.domain.shoppingbasket.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ShoppingBasketResponseDto {

    private final String storeName;

    private final Map<String, Integer> menuList;

    private final List<String> imageUrls;

    private final Integer price;

    public ShoppingBasketResponseDto(String storeName, Map<String, Integer> menuList, List<String> imageUrls, Integer price) {
        this.storeName = storeName;
        this.menuList = menuList;
        this.imageUrls = imageUrls;
        this.price = price;
    }
}
