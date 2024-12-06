package com.kdg.hexa_delivery.domain.coupon.controller;

import com.kdg.hexa_delivery.domain.coupon.dto.CouponRequestDto;
import com.kdg.hexa_delivery.domain.coupon.dto.CouponResponseDto;
import com.kdg.hexa_delivery.domain.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners/coupons")
public class OwnerCouponController {

    private final CouponService couponService;

    @Autowired
    public OwnerCouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    /**
     * 쿠폰 생성 API
     *
     * @param couponRequestDto 쿠폰 생성 정보 dto
     *
     * @return 쿠폰 생성 성공 메세지 전달
     */
    @PostMapping
    public ResponseEntity<CouponResponseDto> createCoupon(@RequestBody CouponRequestDto couponRequestDto) {

        CouponResponseDto couponResponseDto = couponService.createCoupon(couponRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponseDto);
    }


    /**
     * 쿠폰 상태 삭제 변경 API
     *
     * @param couponId 쿠폰 ID
     *
     * @return 쿠폰 삭제 성공 메세지 전달
     */
    @PatchMapping("{couponId}")
    public ResponseEntity<String> deleteStatusCoupon(@PathVariable Long couponId) {

        couponService.deleteStatusCoupon(couponId);

        return ResponseEntity.ok("쿠폰 삭제에 성공했습니다.");
    }
}
