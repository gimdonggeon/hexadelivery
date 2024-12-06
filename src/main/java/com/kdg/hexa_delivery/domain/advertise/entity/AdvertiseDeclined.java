package com.kdg.hexa_delivery.domain.advertise.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "advertisedeclined")
public class AdvertiseDeclined {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AdvertiseDeclinedId;

    @OneToOne
    @JoinColumn(name = "advertise_id")
    private Advertise advertise;

    private String declinedReason;

    public AdvertiseDeclined() {

    }

    public AdvertiseDeclined(Advertise advertise, String declinedReason) {
        this.advertise = advertise;
        this.declinedReason = declinedReason;
    }
}
