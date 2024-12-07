package com.kdg.hexa_delivery.domain.coupon.repository;

import com.kdg.hexa_delivery.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

}
