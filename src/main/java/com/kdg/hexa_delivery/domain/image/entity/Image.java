package com.kdg.hexa_delivery.domain.image.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
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
