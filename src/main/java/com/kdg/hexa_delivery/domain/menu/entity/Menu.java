package com.kdg.hexa_delivery.domain.menu.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(String name, Integer price, String description, Status status, Store store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        updateStore(store);
    }

    public Menu() {

    }


    public void updateStore(Store store) {
        this.store = store;
        store.addMenuList(this);
    }

    public void updateMenu(String name, Integer price, String description) {
        if(name != null && !name.isEmpty()){
            this.name = name;
        }
        if (price != null && price > 0){
            this.price = price;
        }
        if(description != null && !description.isEmpty()){
            this.description = description;
        }

    }

    public void updateStatus2Delete() {
        this.status = Status.DELETED;
    }
}
