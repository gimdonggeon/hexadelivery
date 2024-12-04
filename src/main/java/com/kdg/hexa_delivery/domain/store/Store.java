package com.kdg.hexa_delivery.domain.store;

import com.kdg.hexa_delivery.domain.base.enums.Closure;
import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "store")
@Getter
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String storeName;
    private String category;
    private String phone;
    private String address;
    private String storeDetail;

    @Enumerated(EnumType.STRING)
    private Closure closure;

}
