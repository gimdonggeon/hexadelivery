package com.kdg.hexa_delivery.domain.advertise.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdvertiseRequestDto {

    @NotNull
    private final Integer bidPrice;

    public AdvertiseRequestDto(Integer bidPrice) {
        this.bidPrice = bidPrice;
    }

}
