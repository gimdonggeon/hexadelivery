package com.kdg.hexa_delivery.domain.point.dto;

import com.kdg.hexa_delivery.domain.point.entity.Point;
import com.kdg.hexa_delivery.global.enums.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PointResponseDto {

    private final Long pointId;

    private final Integer pointTotalAmount;

    private final LocalDate expirationTime;

    private final Long orderId;

    private final Status status;


    public PointResponseDto(Long pointId, Integer pointTotalAmount, LocalDate expirationTime, Long orderId, Status status) {
        this.pointId = pointId;
        this.pointTotalAmount = pointTotalAmount;
        this.expirationTime = expirationTime;
        this.orderId = orderId;
        this.status = status;
    }

    public static PointResponseDto toDto(Point point) {
        return new PointResponseDto(
                point.getPointId(),
                point.getPointTotalAmount(),
                point.getExpirationTime(),
                point.getOrder().getId(),
                point.getStatus()
        );
    }
}
