package com.kdg.hexa_delivery.domain.order.repository;

import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

   default Order findByIdOrElseThrow(Long orderId){
        return findById(orderId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }
}

