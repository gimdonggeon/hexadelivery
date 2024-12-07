package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrdersAmountResponseDto {
    private Long ordersCount;

    public OrdersAmountResponseDto(Long ordersCount) {
        this.ordersCount = ordersCount;
    }

}
