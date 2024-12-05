package com.kdg.hexa_delivery.domain.order.repository;

import com.kdg.hexa_delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주어진 ID로 주문 조회
    default Order findByIdOrElseThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
    }

    default Order findByIdAndUserIdOrElseThrow(Long orderId, Long userId) {
        return findById(orderId).filter(order -> order.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("해당 주문은 사용자의 주문이 아닙니다."));
    }

    default Order findByAndStoreIdOrElseThrow(Long orderId, Long storeId) {
        return findById(orderId).filter(order -> order.getStore().getStoreId().equals(storeId))
                .orElseThrow(() -> new RuntimeException("해당 주문은 가게의 주문이 아닙니다."));
    }

    // 메뉴가 삭제되어도 조회할 수 있도록 처리
    @Override
    Optional<Order> findById(Long orderId);
}