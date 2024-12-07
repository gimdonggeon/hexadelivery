package com.kdg.hexa_delivery.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderDeclinedRequestDto {

    private String declinedReason;

    public OrderDeclinedRequestDto(String declinedReason) {
        this.declinedReason = declinedReason;
    }
}
