package com.kdg.hexa_delivery.domain.coupon.dto;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import com.kdg.hexa_delivery.domain.coupon.entity.enums.CouponType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponseDto {

    private final Long couponId;

    private final CouponType couponType;

    private final LocalDateTime expirationTime;

    private final Integer maxDiscountAmount;

    private final Integer totalQuantity;

    private final Integer toDayQuantity;

    private final Status status;

    public CouponResponseDto(Long couponId, CouponType couponType, LocalDateTime expirationTime, Integer maxDiscountAmount, Integer totalQuantity, Integer toDayQuantity, Status status) {
        this.couponId = couponId;
        this.couponType = couponType;
        this.expirationTime = expirationTime;
        this.maxDiscountAmount = maxDiscountAmount;
        this.totalQuantity = totalQuantity;
        this.toDayQuantity = toDayQuantity;
        this.status = status;
    }


    public static CouponResponseDto toDto(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getCouponId(),
                coupon.getCouponType(),
                coupon.getExpirationTime(),
                coupon.getMaxDiscountAmount(),
                coupon.getTotalQuantity(),
                coupon.getToDayQuantity(),
                coupon.getStatus()
        );
    }
}
