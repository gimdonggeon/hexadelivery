package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private final Long id;
    private final Long storeId;
    private final Long menuId;
    private final String menuName;
    private final Integer totalPrice;
    private final Integer quantity;
    private final OrderStatus orderStatus;
    private final String storeOpeningHours;
    private final String storeClosingHours;
    private final Integer storeMinimumOrderValue;
    private final LocalDateTime orderedAt;
    private final LocalDateTime statusChangedAt;
    private final Long countOrder;

    public static OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getStore().getStoreId(),
                order.getMenu().getId(),
                order.getMenu().getName(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.getOrderStatus(),
                order.getStore().getOpeningHours(),
                order.getStore().getClosingHours(),
                order.getStore().getMinimumOrderValue(),
                order.getCreatedAt(),
                order.getStatusChangedAt(),
                order.getOrderCount()
        );
    }


    public OrderResponseDto(Long id, Long storeId, Long menuId,
                            String menuName, Integer totalPrice,Integer quantity,
                            OrderStatus orderStatus,String storeOpeningHours,
                            String storeClosingHours, Integer storeMinimumOrderValue,
                            LocalDateTime orderedAt, LocalDateTime statusChangedAt,
                            Long countOrder) {
        this.id = id;
        this.storeId = storeId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.storeOpeningHours = storeOpeningHours;
        this.storeClosingHours = storeClosingHours;
        this.storeMinimumOrderValue = storeMinimumOrderValue;
        this.orderedAt = orderedAt;
        this.statusChangedAt = statusChangedAt;
        this.countOrder = countOrder;
    }
}
