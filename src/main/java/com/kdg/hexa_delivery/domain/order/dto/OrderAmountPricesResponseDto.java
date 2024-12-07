package com.kdg.hexa_delivery.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderAmountPricesResponseDto {

    private Long ordersAmountPrice;

    public OrderAmountPricesResponseDto(Long ordersAmountPrice) {
        this.ordersAmountPrice = ordersAmountPrice;
    }

}
