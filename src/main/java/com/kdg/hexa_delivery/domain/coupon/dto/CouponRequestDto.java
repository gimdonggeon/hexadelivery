package com.kdg.hexa_delivery.domain.coupon.dto;

import com.kdg.hexa_delivery.domain.coupon.entity.enums.CouponType;
import lombok.Getter;

@Getter
public class CouponRequestDto {

    private final CouponType couponType;

    private final Integer amount;

    private final Integer maxDiscountAmount;

    private final Integer totalQuantity;

    private final Integer toDayQuantity;

    private final Integer periodDayQuantity;

    private final Long StoreId;

    public CouponRequestDto(CouponType couponType, Integer amount, Integer maxDiscountAmount, Integer totalQuantity, Integer toDayQuantity, Integer periodDayQuantity, Long storeId) {
        this.couponType = couponType;
        this.amount = amount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.totalQuantity = totalQuantity;
        this.toDayQuantity = toDayQuantity;
        this.periodDayQuantity = periodDayQuantity;
        StoreId = storeId;
    }
}
