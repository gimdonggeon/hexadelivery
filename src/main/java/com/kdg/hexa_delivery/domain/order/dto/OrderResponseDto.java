package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private Long menuId;
    private Long userId;
    private Long storeId;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private Integer quantity;
    private LocalDateTime orderTime;
    private LocalDateTime orderModifiedTime;

    // 주문 엔티티를 받아 DTO를 생성하는 생성자
    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.menuId = order.getMenu().getId();
        this.userId = order.getUser().getId();
        this.storeId = order.getStore().getStoreId();
        this.orderStatus = order.getOrderStatus();
        this.totalPrice = order.getTotalPrice();
        this.quantity = order.getQuantity();
        this.orderTime = order.getOrderTime();
        this.orderModifiedTime = order.getOrderModifiedTime();
    }
}
