package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private Long id;
    private Long menuId;
    private Long userId;
    private Long storeId;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private final String menuName;
    private final String menuStatus;

    // 주문 엔티티를 받아 DTO를 생성하는 생성자
    public OrderResponseDto(Long id, Long menuId,
                            Long userId, Long storeId,
                            OrderStatus orderStatus, Integer totalPrice,
                            Integer quantity, LocalDateTime createdAt,
                            LocalDateTime modifiedAt, String menuName, String menuStatus) {
        this.id = id;
        this.menuId = menuId;
        this.userId = userId;
        this.storeId = storeId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.menuName = menuName;
        this.menuStatus = menuStatus;
    }

    public static OrderResponseDto toDto(Order order){
        Menu menu = order.getMenu();
         return new OrderResponseDto(
                 order.getId(),
                 order.getMenu().getId(),
                 order.getUser().getId(),
                 order.getStore().getStoreId(),
                 order.getOrderStatus(),
                 order.getTotalPrice(),
                 order.getQuantity(),
                 order.getCreatedAt(),
                 order.getModifiedAt(),
                 menu.getName(),
                 menu.getStatus().name()
         );
    }
}
