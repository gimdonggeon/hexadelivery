package com.kdg.hexa_delivery.domain.coupon.controller;

import com.kdg.hexa_delivery.domain.coupon.dto.CouponResponseDto;
import com.kdg.hexa_delivery.domain.coupon.service.CouponService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/coupons")
public class CustomerCouponController {

    private final CouponService couponService;

    @Autowired
    public CustomerCouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    /**
     * 쿠폰 정보 가져오기 API
     *
     * @param couponId 쿠폰 ID
     *
     * @return 쿠폰 정보 전달
     */
    @GetMapping
    public ResponseEntity<CouponResponseDto> getCoupon(@RequestParam Long couponId) {
        // 쿠폰 발급하기
        CouponResponseDto couponResponseDto = couponService.getCoupon(couponId);

        return ResponseEntity.ok().body(couponResponseDto);

    }

    /**
     * 한 가게 쿠폰 정보 가져오기 API
     *
     * @param storeId 가게 ID
     *
     * @return 쿠폰 정보 전달
     */
    @GetMapping("/stores")
    public ResponseEntity<List<CouponResponseDto>> getStoreCoupon(@RequestParam Long storeId) {
        // 쿠폰 발급하기
        List<CouponResponseDto> couponResponseDtoList = couponService.getMyStoreCoupon(storeId);

        return ResponseEntity.ok().body(couponResponseDtoList);

    }

    /**
     * 쿠폰 발급 API
     *
     * @param couponId 쿠폰 ID
     *
     * @param httpServletRequest  request 객체
     *
     * @return 쿠폰 정보 전달
     */
    @PostMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> issueCoupon(@PathVariable Long couponId,
                                                         HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 쿠폰 발급하기
        CouponResponseDto couponResponseDto = couponService.issueCoupon(loginUser, couponId);

        return ResponseEntity.ok().body(couponResponseDto);

    }


}
