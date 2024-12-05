package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumer/")
public class ConsumerOrderController {

    private final OrderService orderService;

    @Autowired
    public ConsumerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        //주문 생성 처리
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 소비자가 자신의 주문을 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        //주문 조회 처리
        OrderResponseDto orderResponseDto = orderService.getOrderByUserAndId(orderId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }
}