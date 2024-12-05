package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner/orders")
public class OwnerOrderController {

    private final OrderService orderService;

    @Autowired
    public OwnerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 가게 사장의 자신의 가게 주문을 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@RequestBody Long orderId,
                                                     HttpSession session) {
        Long storeId = (Long) session.getAttribute("storeId");

        // 주문 조회 처리
        OrderResponseDto orderResponseDto = orderService.getOrderByStoreAndId(orderId, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

}
