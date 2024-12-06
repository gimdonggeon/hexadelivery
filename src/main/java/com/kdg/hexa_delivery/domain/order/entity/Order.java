package com.kdg.hexa_delivery.domain.order.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
//import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "\"order\"")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne(mappedBy = "order")
    private Review review;

    public void updateTotalPrice() {

        this.totalPrice = this.menu.getPrice() * this.quantity;
    }

    public Order() {
    }

    public Order(User user, Store store, Menu menu, Integer quantity) {
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.quantity = quantity;
        updateTotalPrice();
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }
    public boolean isTotalPriceAboveMinimum() {
        return this.totalPrice >= this.store.getMinimumOrderValue();
    }
}
