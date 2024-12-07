package com.kdg.hexa_delivery.domain.coupon.service;

import com.kdg.hexa_delivery.domain.coupon.dto.CouponRequestDto;
import com.kdg.hexa_delivery.domain.coupon.dto.CouponResponseDto;
import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import com.kdg.hexa_delivery.domain.coupon.entity.UserCoupon;
import com.kdg.hexa_delivery.domain.coupon.entity.enums.CouponType;
import com.kdg.hexa_delivery.domain.coupon.repository.CouponRepository;
import com.kdg.hexa_delivery.domain.coupon.repository.UserCouponRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;
    private final UserCouponRepository userCouponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository,
                         StoreRepository storeRepository,
                         UserCouponRepository userCouponRepository) {

        this.couponRepository = couponRepository;
        this.storeRepository = storeRepository;
        this.userCouponRepository = userCouponRepository;
    }

    /**
     *  쿠폰 생성 메서드
     *
     * @param couponRequestDto 쿠폰 정보 dto
     *
     * @return CouponResponseDto 쿠폰 정보 전달
     */
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        Store store = storeRepository.findByIdOrElseThrow(couponRequestDto.getStoreId());

        Coupon coupon = new Coupon(couponRequestDto.getCouponType(),
                LocalDateTime.now().plusDays(couponRequestDto.getPeriodDayQuantity()),
                couponRequestDto.getAmount(),
                couponRequestDto.getMaxDiscountAmount(),
                couponRequestDto.getTotalQuantity(),
                couponRequestDto.getToDayQuantity(),
                store
        );

        couponRepository.save(coupon);

        return CouponResponseDto.toDto(coupon);
    }

    /**
     *  단순 쿠폰 정보 가져오는 메서드
     *
     * @param couponId 쿠폰 ID
     *
     * @return 쿠폰 정보 전달
     */
    public CouponResponseDto getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(couponId);

        return CouponResponseDto.toDto(coupon);
    }

    /**
     *  내 가게에서 생성한 쿠폰 정보 가져오는 메서드
     *
     * @param storeId 가게 ID
     *
     * @return 쿠폰 정보 전달
     */
    public List<CouponResponseDto> getMyStoreCoupon(Long storeId) {
        List<Coupon> coupons = couponRepository.findAllByStoreId(storeId);

        if(coupons == null){
            throw new RuntimeException("coupon doesn't exist");
        }

        return coupons.stream().map(CouponResponseDto::toDto).toList();
    }

    /**
     * 나의 쿠폰 가져오기 메서드
     *
     * @param userId  쿠폰 ID
     */
    public List<CouponResponseDto> getMyCoupon(Long userId) {
        List<Coupon> coupons = userCouponRepository.findAllByUserId(userId);

        if(coupons == null){
            throw new RuntimeException("coupon doesn't exist");
        }

        return coupons.stream().map(CouponResponseDto::toDto).toList();
    }


    /**
     * 쿠폰 발급하는 메서드
     *
     * @param loginUser 로그인된 유저 정보
     * @param couponId 쿠폰 ID
     *
     * @return 쿠폰 정보 전달
     */
    @Transactional
    public CouponResponseDto issueCoupon(User loginUser, Long couponId) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(couponId);

        if(coupon.getToDayQuantity() > 0 && coupon.getTotalQuantity() > 0) {
            coupon.decrementToDayQuantity();
            coupon.decrementTotalQuantity();
        }

        if(coupon.getToDayQuantity() <= 0 || coupon.getTotalQuantity() <= 0) {
            throw new RuntimeException("할당된 수량이 다 떨어졌습니다.");
        }

        // 유저 쿠폰 생성
        UserCoupon userCoupon = new UserCoupon(coupon, loginUser);

        // 유저에게 쿠폰 발급
        coupon.updateUserCoupon(userCoupon);

        couponRepository.save(coupon);

        return CouponResponseDto.toDto(coupon);
    }


    /**
     * 쿠폰 사용 메서드
     *
     * @param couponId 쿠폰 ID
     * @param userId 유저 ID
     * @param totalPrice 주문 총 금액
     *
     * @return 할인 금액
     */
    public int useCoupon(Long couponId, Long userId, int totalPrice) {
        Coupon coupon = couponRepository.findByIdAndUserIdAndDate(couponId, LocalDateTime.now());

        UserCoupon userCoupon = coupon.getUserCouponList().stream()
                .filter(uc -> uc.getUser().getId().equals(userId)).findAny().orElse(null);
        // 쿠폰이 없거나, 자신이 가진 쿠폰이 아닐경우
        if(userCoupon == null){
            return 0;
        }

        // 사용 여부 등록
        userCoupon.usedCoupon();

        // 정액제일 경우는 해당 금액만 할인
        if(coupon.getCouponType().equals(CouponType.AMOUNT)) {
            return coupon.getAmount();
        }
        // 정률제일 경우에 할인 비율이 최대 할인 금액보다 크면 최대 할인 금액만 할인
        if(coupon.getMaxDiscountAmount() < totalPrice / coupon.getAmount()){
            return coupon.getMaxDiscountAmount();
        }
        // 정률제일 경우는 총 금액에서 할인 비율만 할인
        return totalPrice / coupon.getAmount();

    }


    /**
     * 쿠폰 상태 삭제 변경 메서드
     *
     * @param couponId  쿠폰 ID
     */
    @Transactional
    public void deleteStatusCoupon(Long couponId) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(couponId);

        // 상태 삭제 변경
        coupon.updateStatus2Delete();

        couponRepository.save(coupon);
    }
}
