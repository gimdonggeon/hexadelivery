package com.kdg.hexa_delivery.domain.user.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.user.enums.Role;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.coupon.entity.UserCoupon;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.point.entity.Point;
import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 320, unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phone;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NORMAL;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Store> storeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> pointList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCouponList = new ArrayList<>();

    private String nickname;

    public User(){
    }

    public User(String email, String nickname) {

        this.email = email;
        this.nickname = nickname;
    }

    public User(Role role, String email, String password, String name, String phone, Status status) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public void updateStatus2Delete(){
        this.status = Status.DELETED;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
