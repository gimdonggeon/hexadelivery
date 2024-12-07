package com.kdg.hexa_delivery.domain.review.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private int rating;

    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NORMAL;

    public Review(User user, Store store, Order order, int rating, String content, Status status) {
        this.user = user;
        this.store = store;
        this.order = order;
        this.rating = rating;
        this.content = content;
        this.status = status;
    }

    public Review() {

    }
}
