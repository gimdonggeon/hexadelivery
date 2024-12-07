package com.kdg.hexa_delivery.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DayAmountRequestDto {

    @NotNull(message = "날짜 선택은 필수입니다.")
    private String date;

    private String category;

    private Long storeId;

    public DayAmountRequestDto(String date, String category, Long storeId) {
        this.date = date;
        this.category = category;
        this.storeId = storeId;
    }

}
