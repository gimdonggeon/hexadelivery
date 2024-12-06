package com.kdg.hexa_delivery.domain.advertise.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.AdvertiseStatus;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "advertise")
public class Advertise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advertiseId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer bidPrice;

    private AdvertiseStatus advertiseStatus;

    private String declinedReason;

    public Advertise() {
    }

    public Advertise(User user, Store store, Integer bidPrice, AdvertiseStatus advertiseStatus) {
        this.user = user;
        this.store = store;
        this.bidPrice = bidPrice;
        this.advertiseStatus = advertiseStatus;
    }

    // 광고신청 수락
    public void acceptAdvertiseStatus() {
        this.advertiseStatus = AdvertiseStatus.ACCEPTED;
    }

    // 광고신청 거부
    public void declineAdvertiseStatus(String declinedReason) {
        this.advertiseStatus = AdvertiseStatus.DECLINED;
        this.declinedReason = declinedReason;
    }

}
