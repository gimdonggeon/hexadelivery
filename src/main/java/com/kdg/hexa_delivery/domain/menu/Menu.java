package com.kdg.hexa_delivery.domain.menu;

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

    private String name;

    private Integer price;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Menu(String name, Integer price, Status status, Store store) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.store = store;
    }

    public Menu() {

    }

    public void updateMenu(String name, Integer price){
        if(name != null && !name.isEmpty()){
            this.name = name;
        }
        if (price != null && price > 0){
            this.price = price;
        }
    }

    public void updateStatus2Delete() {
        this.status = Status.DELETE;
    }
}
