package com.kdg.hexa_delivery.domain.store.entity;

import com.kdg.hexa_delivery.domain.base.enums.State;
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
    private State state;

    public Store(User user, String storeName, String category, String phone, String address, String storeDetail, State state) {
        this.user = user;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.state = state;

    }

    public Store() {

    }


    public void updateStore(String storeName, String category,
                            String phone, String address,
                            String storeDetail) {
        if(storeName != null && !storeName.isEmpty()) {
            this.storeName = storeName;
        }
        if(category != null && !category.isEmpty()) {
            this.category = category;
        }if(phone != null && !phone.isEmpty()) {
            this.phone = phone;
        }
        if(address != null && !address.isEmpty()) {
            this.address = address;
        }
        if(storeDetail != null && !storeDetail.isEmpty()) {
            this.storeDetail = storeDetail;
        }
    }

    public void updateStoreStatus(){
        this.state = State.CLOSURE;
    }
}
