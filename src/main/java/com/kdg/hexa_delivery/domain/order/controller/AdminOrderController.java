package com.kdg.hexa_delivery.domain.order.controller;

import com.kdg.hexa_delivery.domain.order.dto.*;
import com.kdg.hexa_delivery.domain.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins/orders")
public class AdminOrderController {
    private final OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    // 배달 어플 전체 통계

    /*
     *
     * 일간 주문 수
     *
     */
    @GetMapping("/day-orders")
    public ResponseEntity<OrdersAmountResponseDto> getDayOrders(@RequestBody DayAmountRequestDto dayAmountRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getDayOrders(dayAmountRequestDto));
    }

    /*
     *
     * 월간 총 주문 수
     *
     */
    @GetMapping("/month-orders")
    public ResponseEntity<OrdersAmountResponseDto> getMonthOrders(@RequestBody MonthAmountRequestDto monthAmountRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getMonthOrders(monthAmountRequestDto));
    }

    /*
     *
     * 일간 총 주문 금액
     *
     */
    @GetMapping("/day-orders-prices")
    public ResponseEntity<OrderAmountPricesResponseDto> getDayOrderPrices(@RequestBody DayAmountRequestDto dayAmountRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getDayOrderPrices(dayAmountRequestDto));
    }

    /*
     *
     * 월간 총 주문 금액
     *
     */
    @GetMapping("/month-orders-prices")
    public ResponseEntity<OrderAmountPricesResponseDto> getMonthOrderPrices(@RequestBody MonthAmountRequestDto monthAmountRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getMonthOrderPrices(monthAmountRequestDto));
    }


}
