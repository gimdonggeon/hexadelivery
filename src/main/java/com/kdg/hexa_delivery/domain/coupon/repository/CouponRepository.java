package com.kdg.hexa_delivery.domain.coupon.repository;

import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    default Coupon findByIdOrElseThrow(Long couponId) {
        return findById(couponId).orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));
    }

    @Query("SELECT c FROM Coupon c WHERE c.store.storeId = :storeId ")
    List<Coupon> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId AND c.expirationTime > :time")
    Coupon findByIdAndUserIdAndDate(@Param("couponId") Long couponId, @Param("time") LocalDateTime time);
}
