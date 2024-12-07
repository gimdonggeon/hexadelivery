package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.global.validation.Validation;
import com.kdg.hexa_delivery.domain.order.dto.OrderDeclinedRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderDeclinedResponseDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderStatusRequestDto;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                                                              @RequestBody OrderStatusRequestDto orderStatusRequestDto,
                                                              HttpServletRequest httpServletRequest) {
        User loginUser = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);

        Order order = orderService.findOrderById(orderId);

        Validation.validMyStoreAccess(order.getStore().getStoreId(), loginUser);

        // 상태 전환 가능 여부 체크
        if (!orderStatusRequestDto.canTransitionTo(order.getOrderStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상태변경은 이전으로 돌아갈 수 없습니다.");
        }


        //주문 상태 업데이트
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(orderId, orderStatusRequestDto.toOrderStatus());

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    // 주문 상세 조회 API(주문 내용 확인)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderDetails(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    // 주문 거절
    @PutMapping("/{orderId}/decline")
    public ResponseEntity<OrderDeclinedResponseDto> declineOrder(@PathVariable Long orderId, @RequestBody OrderDeclinedRequestDto orderDeclinedRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.declineOrder(orderId, orderDeclinedRequestDto));
    }
}