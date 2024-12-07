package com.kdg.hexa_delivery.domain.advertise.dto;

import com.kdg.hexa_delivery.domain.advertise.entity.Advertise;
import com.kdg.hexa_delivery.domain.advertise.enums.AdvertiseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdvertiseDeclinedResponseDto {

    private final Long advertiseId;

    private final String declinedReason;

    private final Integer bidPrice;

    private final AdvertiseStatus advertiseStatus;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public AdvertiseDeclinedResponseDto(Long advertiseId, String declinedReason,
                                        Integer bidPrice, AdvertiseStatus advertiseStatus,
                                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.advertiseId = advertiseId;
        this.declinedReason = declinedReason;
        this.bidPrice = bidPrice;
        this.advertiseStatus = advertiseStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static AdvertiseDeclinedResponseDto toDto(Advertise advertise) {
        return new AdvertiseDeclinedResponseDto(
                advertise.getAdvertiseId(),
                advertise.getDeclinedReason(),
                advertise.getBidPrice(),
                advertise.getAdvertiseStatus(),
                advertise.getCreatedAt(),
                advertise.getModifiedAt()
        );
    }

}
