package com.kdg.hexa_delivery.domain.coupon.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.coupon.entity.enums.UserCouponStatus;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user-coupon")
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;

    @Enumerated(EnumType.STRING)
    private UserCouponStatus status;

    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserCoupon(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        this.status = UserCouponStatus.UNUSED;
    }

    public UserCoupon() {

    }

    public void usedCoupon(){
        this.status = UserCouponStatus.USED;
        this.usedAt = LocalDateTime.now();
    }
}
