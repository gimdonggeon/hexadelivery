package com.kdg.hexa_delivery.domain.base.enums;

public enum OrderStatus {
    ORDERED, ACCEPTED, COOKING, COOKED, DELIVERY, DELIVERED , DECLINED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case ORDERED:
                return newStatus == ACCEPTED;
            case ACCEPTED:
                return newStatus == COOKING;
            case COOKING:
                return newStatus == COOKED;
            case COOKED:
                return newStatus == DELIVERY;
            case DELIVERY:
                return newStatus == DELIVERED;
            default:
                return false;
        }
    }
}
