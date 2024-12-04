package com.kdg.hexa_delivery.domain.order;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


}
