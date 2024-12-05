package com.kdg.hexa_delivery.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderRequestDto {

    private Long menuId;
    private Long storeId;
    private Integer quantity;

    public OrderRequestDto() {
    }

    public OrderRequestDto(Long menuId, Long storeId, Integer quantity) {
        this.menuId = menuId;
        this.storeId = storeId;
        this.quantity = quantity;
    }
}