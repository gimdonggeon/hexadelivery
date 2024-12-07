package com.kdg.hexa_delivery.domain.order.dto;

import com.kdg.hexa_delivery.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

    @NotNull(message = "주문 상태는 필수입니다.")
    private OrderStatus orderStatus;

    public OrderStatusRequestDto() {
    }

    public OrderStatus toOrderStatus() {
        return this.orderStatus;
    }

    // 상태 전환이 가능한 지 체크
    public boolean canTransitionTo(OrderStatus currentStatus) {
        return currentStatus.canTransitionTo(this.orderStatus);
    }
}
