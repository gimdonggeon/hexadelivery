package com.kdg.hexa_delivery.domain.store.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import com.kdg.hexa_delivery.domain.coupon.entity.Coupon;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


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
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable=false)
    private String phone;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String storeDetail;

    @Column(nullable=false)
    private String openingHours;

    @Column(nullable = false)
    private String closingHours;

    private Integer minimumOrderValue;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Coupon> CouponList = new ArrayList<>();

    public Store(User user, String storeName, Category category, String phone, String address,
                 String storeDetail, String openingHours, String closingHours,
                 Integer minimumOrderValue, Status status) {

        updateUser(user);
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
        this.minimumOrderValue = minimumOrderValue;
        this.status = status;

    }

    public Store() {

    }

    public void updateUser(User user){
        this.user = user;
        user.getStoreList().add(this);
    }

    public void addMenuList(Menu menu){
        this.menuList.add(menu);
    }


    public void updateStore(String storeName, Category category,
                            String phone, String address,
                            String storeDetail, String openingHours,
                            String closingHours, Integer minimumOrderValue) {
        if(storeName != null && !storeName.isEmpty()) {
            this.storeName = storeName;
        }
        if(category != null) {
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
        if(openingHours != null && !openingHours.isEmpty()) {
            this.openingHours = openingHours;
        }
        if(closingHours != null && !closingHours.isEmpty()) {
            this.closingHours = closingHours;
        }

        this.minimumOrderValue = minimumOrderValue;
    }

    public void updateStoreStatus(){
        this.status = Status.DELETED;
    }
}
