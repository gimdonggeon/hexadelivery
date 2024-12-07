package com.kdg.hexa_delivery.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DayAmountRequestDto {

    private String date;

    private String category;

    private Long storeId;

    public DayAmountRequestDto(String date, String category, Long storeId) {
        this.date = date;
        this.category = category;
        this.storeId = storeId;
    }

}
