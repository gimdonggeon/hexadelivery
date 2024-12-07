package com.kdg.hexa_delivery.domain.advertise.dto;

import com.kdg.hexa_delivery.domain.advertise.entity.Advertise;
import com.kdg.hexa_delivery.domain.advertise.enums.AdvertiseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdvertiseResponseDto {

    private final Long advertiseId;

    private final Long userId;

    private final Long storeId;

    private final Integer bidPrice;

    private final AdvertiseStatus advertiseStatus;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public AdvertiseResponseDto(Long advertiseId, Long userId,
                                Long storeId, Integer bidPrice,
                                AdvertiseStatus advertiseStatus,LocalDateTime createdAt,
                                LocalDateTime modifiedAt) {
        this.advertiseId = advertiseId;
        this.userId = userId;
        this.storeId = storeId;
        this.bidPrice = bidPrice;
        this.advertiseStatus = advertiseStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }

    public static AdvertiseResponseDto toDto(Advertise advertise) {
        return new AdvertiseResponseDto(
                advertise.getAdvertiseId(),
                advertise.getUser().getId(),
                advertise.getStore().getStoreId(),
                advertise.getBidPrice(),
                advertise.getAdvertiseStatus(),
                advertise.getCreatedAt(),
                advertise.getModifiedAt()

        );
    }

}
