package com.kdg.hexa_delivery.domain.coupon.repository;

import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    default Coupon findByIdOrElseThrow(Long couponId) {
        return findById(couponId).orElseThrow(() -> new NotFoundException(ExceptionType.COUPON_NOT_FOUND));
    }

    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId AND c.status = 'NORMAL'")
    Coupon findAllByCouponIdAndNORMAL(@Param("couponId") Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.store.storeId = :storeId ")
    List<Coupon> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId AND c.status = 'NORMAL'")
    Coupon findByIdAndCouponId(@Param("couponId") Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.status = 'NORMAL'")
    List<Coupon> findAllByNORMAL();

    @Query("SELECT c FROM Coupon c WHERE c.store.storeId = :storeId AND c.expirationTime >= :now AND c.status = 'NORMAL'")
    List<Coupon> findAllByStoreIdAndDate(@Param("storeId") Long storeId, @Param("now") LocalDate now);
}
