package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusDto {

    @NotNull(message = "주문 상태는 필수입니다.")
    private OrderStatus orderStatus;

    public OrderStatusDto() {
    }

    public OrderStatusDto(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatusDto(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public OrderStatus toOrderStatus() {
        return this.orderStatus;
    }

    // 상태 전환이 가능한 지 체크
    public boolean canTransitionTo(OrderStatus currentStatus) {
        return currentStatus.canTransitionTo(this.orderStatus);
    }
}
