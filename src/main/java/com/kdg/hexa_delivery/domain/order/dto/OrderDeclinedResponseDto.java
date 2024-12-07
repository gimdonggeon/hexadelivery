package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDeclinedResponseDto {

    private Long id;

    private Long userId;

    private String storeName;

    private String menu;

    private OrderStatus status;

    private Integer quantity;

    private Integer totalPrice;

    private LocalDateTime statusChangeAt;

    private String declineReason;

    public OrderDeclinedResponseDto(Long id, Long userId,
                                    String storeName, String menu,
                                    Integer totalPrice, Integer quantity,
                                    OrderStatus status, LocalDateTime statusChangeAt,
                                    String declineReason){
        this.id = id;
        this.userId = userId;
        this.storeName = storeName;
        this.menu = menu;
        this.status = status;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.statusChangeAt = statusChangeAt;
        this.declineReason = declineReason;
    }

    public static OrderDeclinedResponseDto toDto(Order order){
        return new OrderDeclinedResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getStore().getStoreName(),
                order.getMenu().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getStatusChangedAt(),
                order.getDeclinedReason()
        );

    }
}
