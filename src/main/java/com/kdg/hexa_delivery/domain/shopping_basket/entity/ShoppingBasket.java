package com.kdg.hexa_delivery.domain.shopping_basket.entity;

import lombok.Getter;

import java.util.Map;

@Getter
public class ShoppingBasket {

    private final Long userId;

    private final Long storeId;

    private final Map<Long, Integer> menuList;

    public ShoppingBasket(Long userId, Long storeId, Map<Long, Integer> menuList) {
        this.userId = userId;
        this.storeId = storeId;
        this.menuList = menuList;
    }
}
