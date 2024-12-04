package com.kdg.hexa_delivery.domain.menu;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.store.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer price;

    @NotBlank
    private Status status;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
