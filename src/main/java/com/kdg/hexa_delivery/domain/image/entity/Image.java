package com.kdg.hexa_delivery.domain.image.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ImageOwner owner;

    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Image() {

    }

    public Image(String imageName, String imageUrl, Long ownerId, ImageOwner owner) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.owner = owner;
    }

    public void updateImage(String imageName, String imageUrl) {
        if(imageName != null) {
            this.imageName = imageName;
        }
        if(imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}
