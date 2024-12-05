package com.kdg.hexa_delivery.domain.user.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
//import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId;

    private Role role;

    @Column(length = 320)
    private String email;

    private String password;

    private String name;

    private String phone;

    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Store> storeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();
    /*
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();
    */
    public User(){

    }
    public User(Role role, String loginId, String email, String password, String name, String phone, Status status) {
        this.role = role;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public void updateStatus2Delete(){
        this.status = Status.DELETED;
    }
}
