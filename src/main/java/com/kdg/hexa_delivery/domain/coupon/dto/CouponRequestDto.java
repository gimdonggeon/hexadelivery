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


    public CouponRequestDto(CouponType couponType, Integer amount, Integer maxDiscountAmount, Integer totalQuantity, Integer toDayQuantity) {
        this.couponType = couponType;
        this.amount = amount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.totalQuantity = totalQuantity;
        this.toDayQuantity = toDayQuantity;
    }
}
