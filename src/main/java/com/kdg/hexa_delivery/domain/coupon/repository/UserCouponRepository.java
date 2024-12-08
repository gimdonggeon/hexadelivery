package com.kdg.hexa_delivery.domain.coupon.repository;

import com.kdg.hexa_delivery.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.id = :userId AND uc.coupon.expirationTime >= :now AND uc.coupon.status = 'NORMAL'")
    List<UserCoupon> findAllByUserId(@Param("userId") Long userId, @Param("now") LocalDate now);

}
