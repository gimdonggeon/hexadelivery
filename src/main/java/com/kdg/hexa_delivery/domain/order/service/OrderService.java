package com.kdg.hexa_delivery.domain.order.service;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.coupon.service.CouponService;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.menu.repository.MenuRepository;
import com.kdg.hexa_delivery.domain.order.dto.OrderRequestDto;
import com.kdg.hexa_delivery.domain.order.dto.OrderResponseDto;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.order.repository.OrderRepository;
import com.kdg.hexa_delivery.domain.point.service.PointService;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PointService pointService;
    private final CouponService couponService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        StoreRepository storeRepository,
                        MenuRepository menuRepository,
                        PointService pointService,
                        CouponService couponService) {

        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.pointService = pointService;
        this.couponService = couponService;
    }

    //고객 주문 생성
    @Transactional
    public OrderResponseDto createOrder(Long storeId, User user, OrderRequestDto orderRequestDto) {

        // 가게 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게가 없습니다."));

        // 헤당 가게의 메뉴 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(orderRequestDto.getMenuId());

        // 개수 가져오기
        int quantity = orderRequestDto.getQuantity();

        // 전체 금액 계산
        int totalPrice = menu.getPrice() * quantity;

        // 주문 시간과 최소 금액 체크
        this.validateOrderTimeAndMinimumAmount(store, totalPrice);

        // 쿠폰 사용 적용
        if(orderRequestDto.getCouponId() != null) {
            totalPrice -= couponService.useCoupon(orderRequestDto.getCouponId(), user.getId(), totalPrice);
        }

        // 포인트 사용 적용
        if(orderRequestDto.getPointDiscount() != null) {
            // 전체금액에서 사용한 포인트만큼 빼기
            totalPrice -= pointService.usePoint(orderRequestDto.getPointDiscount());
        }

        // 주문 생성
        Order order = new Order(user, store, menu, totalPrice, quantity);

        //주문 저장
        orderRepository.save(order);

        return OrderResponseDto.toDto(order);
    }

    // 고객의 모든 주문 조회
    public List<OrderResponseDto> getAllOrderByUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(OrderResponseDto::toDto)
                .collect(Collectors.toList());
    }

    // 주문 시간과 최소 금액 체크
    public void validateOrderTimeAndMinimumAmount(Store store, int totalPrice) {

        // 가게의 오픈시간과 클로즈 시간 체크
        String openingHoursString = store.getOpeningHours();
        String closingHoursString = store.getClosingHours();

        LocalTime openingHours = LocalTime.parse(openingHoursString);
        LocalTime closingHours = LocalTime.parse(closingHoursString);

        LocalTime now = LocalTime.now();
        if (now.isBefore(openingHours) || now.isAfter(closingHours)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가게 영업시간이 아닙니다.");
        }

        // 최소금액 체크
        if (totalPrice < store.getMinimumOrderValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "최소 주문 금액보다 높아야 합니다.");
        }
    }


    // 주문 상태 업데이트
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        if (!order.getOrderStatus().canTransitionTo(newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이전 단계로 돌아갈 수 없습니다.");
        }
        order.updateStatus(newStatus);
        orderRepository.save(order);

        // 배달 완료시 포인트 적립
        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)){
            pointService.addPoint(order);
        }

        return OrderResponseDto.toDto(order);
    }


    // 주문 상세 조회
    public OrderResponseDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
        return OrderResponseDto.toDto(order);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
    }

    //포인트
}
