package com.kdg.hexa_delivery.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MonthAmountRequestDto {
    @NotNull(message = "날짜 선택은 필수입니다.")
    private String startDate;

    @NotNull(message = "날짜 선택은 필수입니다.")
    private String endDate;

    private String category;

    private Long storeId;

    public MonthAmountRequestDto(String startDate, String endDate, String category, Long storeId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.storeId = storeId;
    }

}
