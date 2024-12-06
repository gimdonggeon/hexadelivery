package com.kdg.hexa_delivery.domain.advertise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdvertiseDeclinedRequestDto {

    @NotNull
    private final String declinedReason;

    public AdvertiseDeclinedRequestDto(String declinedReason) {
        this.declinedReason = declinedReason;
    }

}
