package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.base.validation.Validation;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners/orders")
public class OwnerOrderController {

    private final OrderService orderService;

    @Autowired
    public OwnerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 상태 변경 API
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody OrderStatus newStatus,
                                                              HttpServletRequest httpServletRequest) {
        User loginUser = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);

        Order order = orderService.findOrderById(orderId);

        Validation.validMyStoreAccess(order.getStore().getStoreId(), loginUser);


        //주문 상태 업데이트
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(orderId, newStatus);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    // 주문 상세 조회 API(주문 내용 확인)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderDetails(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }
}