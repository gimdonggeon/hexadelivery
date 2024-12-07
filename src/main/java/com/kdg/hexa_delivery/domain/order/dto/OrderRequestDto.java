package com.kdg.hexa_delivery.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    @NotNull(message = "메뉴 이름은 필수입니다.")
    private final Long menuId;

    @NotNull
    @Min(value = 1, message = "수량은 최소1개입니다.")
    private final Integer quantity;

    private final Integer pointDiscount;

    private final Long couponId;

    public OrderRequestDto(Long menuId, Integer quantity, Integer pointDiscount, Long couponId) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.pointDiscount = pointDiscount;
        this.couponId = couponId;
    }
}