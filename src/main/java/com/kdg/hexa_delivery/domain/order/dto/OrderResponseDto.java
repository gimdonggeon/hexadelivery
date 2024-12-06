package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private Long id;
    private Long storeId;
    private Long menuId;
    private String menuName;
    private Integer totalPrice;
    private Integer quantity;
    private OrderStatus orderStatus;
    private String storeOpeningHours;
    private String storeClosingHours;
    private Integer storeMinimumOrderValue;

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
                order.getStore().getMinimumOrderValue()
        );
    }


    public OrderResponseDto(Long id, Long storeId, Long menuId,
                            String menuName, Integer totalPrice,Integer quantity,
                            OrderStatus orderStatus,String storeOpeningHours,
                            String storeClosingHours, Integer storeMinimumOrderValue) {
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
    }
}
