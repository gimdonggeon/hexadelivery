package com.kdg.hexa_delivery.domain.point.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Column(nullable = false)
    private Integer pointTotalAmount;

    @Column(nullable = false)
    private Integer pointPresentAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 현재로부터 한달 뒤
    @Column(nullable = false)
    private LocalDate expirationTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public Point(Order order, User user, Integer pointTotalAmount) {
        this.order = order;
        updateUser(user);
        this.pointTotalAmount = pointTotalAmount;
        this.pointPresentAmount = pointTotalAmount;
        this.expirationTime = LocalDate.now().plusMonths(1);
        this.status = Status.NORMAL;
    }

    public Point() {

    }

    public void discountPointAmount(Integer pointDiscount) {
        this.pointPresentAmount -= pointDiscount;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getPointList().add(this);
    }

    public void updateStatus2Delete() {
        this.status = Status.DELETED;
    }
}
