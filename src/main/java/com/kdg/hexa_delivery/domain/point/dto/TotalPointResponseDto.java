package com.kdg.hexa_delivery.domain.point.dto;

import lombok.Getter;

@Getter
public class TotalPointResponseDto {

    private final int totalPoints;

    public TotalPointResponseDto(int totalPoints) {
        this.totalPoints = totalPoints;
    }

}
