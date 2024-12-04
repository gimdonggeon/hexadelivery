package com.kdg.hexa_delivery.domain.store.entity;

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

    @Column(nullable=false)
    private String storeName;

    @Column(nullable=false)
    private String category;

    @Column(nullable=false)
    private String phone;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String storeDetail;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Closure closure;

    public Store(User user, String storeName, String category, String phone, String address, String storeDetail, Closure closure) {
        this.user = user;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.closure = closure;

    }

    public Store() {

    }


    public void updateStore(String storeName, String category,
                            String phone, String address,
                            String storeDetail) {
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;

    }

    public void updateStoreStatus(){
        this.closure = Closure.CLOSURE;
    }
}
