package com.kdg.hexa_delivery.domain.coupon.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.coupon.entity.enums.CouponType;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "coupon")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    private Integer amount;

    // 현재로부터 일주일 뒤
    @Column(nullable = false)
    private LocalDate expirationTime;

    private Integer maxDiscountAmount;

    @Column(nullable = false)
    private Integer totalQuantity;

    private Integer todayQuantity;

    private Integer todayFixQuantity;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserCoupon> userCouponList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public Coupon(
                  CouponType couponType,
                  LocalDate expirationTime,
                  Integer amount,
                  Integer maxDiscountAmount,
                  Integer totalQuantity,
                  Integer todayQuantity,
                  Store store) {

        this.couponType = couponType;
        this.expirationTime = expirationTime;
        this.amount = amount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.totalQuantity = totalQuantity;
        this.todayQuantity = todayQuantity;
        this.todayFixQuantity = todayQuantity;
        status = Status.NORMAL;
        updateStore(store);

    }

    public Coupon() {

    }

    public void updateStore(Store store) {
        this.store = store;
        store.getCouponList().add(this);
    }

    public void updateUserCoupon(UserCoupon userCoupon) {
        this.userCouponList.add(userCoupon);
    }

    public void resetToDayQuantity(){
        this.todayQuantity = todayFixQuantity;
    }

    public void decrementToDayQuantity(){
        this.todayQuantity--;
    }


    public void decrementTotalQuantity(){
        this.totalQuantity--;
    }

    public void updateStatus2Delete() {
        this.status = Status.DELETED;
    }
}
