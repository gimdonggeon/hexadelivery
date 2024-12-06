package com.kdg.hexa_delivery.domain.image.dto;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import lombok.Getter;

@Getter
public class ImageRequestDto {

    private final Long OwnerId;

    private final ImageOwner owner;

    public ImageRequestDto(Long ownerId, ImageOwner owner) {
        OwnerId = ownerId;
        this.owner = owner;
    }
}
