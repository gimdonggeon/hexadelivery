package com.kdg.hexa_delivery.domain.coupon.repository;

import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    default Coupon findByIdOrElseThrow(Long couponId) {
        return findById(couponId).orElseThrow(() -> new NotFoundException(ExceptionType.COUPON_NOT_FOUND));
    }

    Coupon findByCouponIdAndStatus(Long couponId, Status status);

    List<Coupon> findAllByStatus(Status status);

    // @Query("SELECT c FROM Coupon c WHERE c.store.storeId = :storeId AND c.expirationTime >= :now AND c.status = :status")
    List<Coupon> findAllByStoreStoreIdAndStatusAndExpirationTimeIsGreaterThan(Long storeId,  Status status, LocalDate now);
}
