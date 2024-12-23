package com.kdg.hexa_delivery.domain.order.controller;


import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/customers/orders")
public class CustomerOrderController {

    private final OrderService orderService;

    @Autowired
    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 고객 주문 생성 API
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                        HttpServletRequest httpServletRequest) {

        User loginUser = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);

        // 주문 생성
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto.getStoreId(), loginUser, orderRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 고객 주문내역 조회 API
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrder(HttpServletRequest httpServletRequest) {

        User loginUser = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);

        List<OrderResponseDto> orders = orderService.getAllOrderByUser(loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    //고객 주문상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getDeclinedOrder(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
        User loginUser = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);
        Map<String,Object> result = orderService.getCustomerOrderDetails(loginUser,orderId);


        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
