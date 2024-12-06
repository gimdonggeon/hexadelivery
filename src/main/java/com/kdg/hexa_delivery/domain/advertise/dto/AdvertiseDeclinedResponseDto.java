package com.kdg.hexa_delivery.domain.advertise.dto;

import com.kdg.hexa_delivery.domain.advertise.entity.AdvertiseDeclined;
import lombok.Getter;

@Getter
public class AdvertiseDeclinedResponseDto {

    private final Long advertiseDeclinedId;

    private final Long advertiseId;

    private final String declinedReason;

    public AdvertiseDeclinedResponseDto(Long advertiseDeclinedId,Long advertiseId, String declinedReason) {
        this.advertiseDeclinedId = advertiseDeclinedId;
        this.advertiseId = advertiseId;
        this.declinedReason = declinedReason;
    }

    public static AdvertiseDeclinedResponseDto toDto(AdvertiseDeclined advertiseDeclined) {
        return new AdvertiseDeclinedResponseDto(
                advertiseDeclined.getAdvertise().getAdvertiseId(),
                advertiseDeclined.getAdvertise().getAdvertiseId(),
                advertiseDeclined.getDeclinedReason()
        );
    }

}
