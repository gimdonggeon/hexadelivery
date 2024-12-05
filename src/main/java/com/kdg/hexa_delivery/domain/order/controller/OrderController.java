package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성
    @PostMapping
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestParam Long userId) {
        return orderService.createOrder(orderRequestDto, userId);
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    // 주문 수량 수정
    @PatchMapping("/{orderId}/quantity")
    public OrderResponseDto updateOrderQuantity(@PathVariable Long orderId, @RequestParam Integer quantity) {
        return orderService.updateOrderQuantity(orderId, quantity);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "주문이 정상적으로 삭제되었습니다.";
    }

    // 모든 주문 조회
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}