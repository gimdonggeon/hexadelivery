package com.kdg.hexa_delivery.domain.order.repository;

import com.kdg.hexa_delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주어진 ID로 주문 조회
    default Order findByIdOrElseThrow(Long orderId){
        return findById(orderId).orElseThrow(()-> new RuntimeException("주문이 존재하지 않습니다."));
    }
}
