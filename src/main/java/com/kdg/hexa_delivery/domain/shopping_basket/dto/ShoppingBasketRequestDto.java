package com.kdg.hexa_delivery.domain.shopping_basket.dto;

import lombok.Getter;

@Getter
public class ShoppingBasketRequestDto {

    private final Long storeId;

    private final Long menuId;

    private final Integer quantity;



    public ShoppingBasketRequestDto(Long storeId, Long menuId, Integer quantity) {
        this.storeId = storeId;
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
