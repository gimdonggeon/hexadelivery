package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody OrderStatus status,
                                                              HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 주문 수량 수정
    @PatchMapping("/{orderId}/quantity")
    public ResponseEntity<OrderResponseDto> updateOrderQuantity(@PathVariable Long orderId,
                                                                @RequestBody Integer quantity) {

        OrderResponseDto orderResponseDto = orderService.updateOrderQuantity(orderId, quantity);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문이 정상적으로 삭제되었습니다.");
    }

    // 모든 주문 조회
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> allOrders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }
}