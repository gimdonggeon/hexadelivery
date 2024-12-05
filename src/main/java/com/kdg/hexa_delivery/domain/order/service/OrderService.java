package com.kdg.hexa_delivery.domain.order.service;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.menu.repository.MenuRepository;
import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.order.repository.OrderRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, StoreRepository storeRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
    }

    // 주문 생성
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long userId) {

        User user = userRepository.findByIdOrElseThrow(userId);

        Store store = storeRepository.findByIdOrElseThrow(orderRequestDto.getStoreId());

        Menu menu = menuRepository.findByIdOrElseThrow(orderRequestDto.getMenuId());


        Order order = new Order(user, store, menu, orderRequestDto.getQuantity());

        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.toDto(savedOrder);
    }

    // 주문 상태 업데이트
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdOrElseThrow(orderId);

        order.updateStatus(status);
        orderRepository.save(order);

        return OrderResponseDto.toDto(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findByIdOrElseThrow(orderId);

        orderRepository.delete(order);

        orderRepository.save(order);
    }

    // 모든 주문 조회
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponseDto::toDto).toList();
    }
}