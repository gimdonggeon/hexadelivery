package com.kdg.hexa_delivery.domain.order.service;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.coupon.service.CouponService;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.menu.repository.MenuRepository;
import com.kdg.hexa_delivery.domain.order.dto.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            totalPrice -= pointService.usePoint(orderRequestDto.getPointDiscount(), user.getId());
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상태 변경은 각 단계를 순차적으로만 진행할 수 있습니다.");
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


    /*
     *
     *  관리자 대시보드 기능
     *
     * */

    // 일간(Day) 주문수 조회 ( 기간 OR 가게 OR 카테고리 )

    public OrdersAmountResponseDto getDayOrders(DayAmountRequestDto dayAmountRequestDto) {

        String dateString = dayAmountRequestDto.getDate();
        // 날짜 검증 및 형변환
        LocalDateTime startDateTime = validDate(dateString);
        LocalDateTime endDateTime = validDateV2(startDateTime);

        return orderRepository.getDayOrders(startDateTime, endDateTime, dayAmountRequestDto.getCategory(), dayAmountRequestDto.getStoreId());
    }

    // 월간(Month) 주문수 조회 ( 기간 OR 가게 OR 카테고리 )
    public OrdersAmountResponseDto getMonthOrders(MonthAmountRequestDto monthAmountRequestDto) {
        // 날짜 형변환
        String startDateString = monthAmountRequestDto.getStartDate();
        String endDateString = monthAmountRequestDto.getEndDate();

        // 월간 조회 날짜 검증
        LocalDateTime startDateTime = validMonth(startDateString);
        LocalDateTime endDateTime = validMonthV2(startDateTime, endDateString);

        return orderRepository.getMonthOrders(startDateTime, endDateTime,
                monthAmountRequestDto.getCategory(), monthAmountRequestDto.getStoreId());
    }
    // 일간(Day) 주문 총액 조회 ( 기간 OR 가게 OR 카테고리 )
    public OrderAmountPricesResponseDto getDayOrderPrices(DayAmountRequestDto dayAmountRequestDto){
        // 날짜 형변환
        String dateString = dayAmountRequestDto.getDate();

        // 날짜 검증 및 형변환
        LocalDateTime startDateTime = validDate(dateString);
        LocalDateTime endDateTime = validDateV2(startDateTime);

        return orderRepository.getDayOrderPrices(startDateTime, endDateTime, dayAmountRequestDto.getCategory(), dayAmountRequestDto.getStoreId());
    }
    // 월간(Month) 주문 총액 조회 ( 기간 OR 가게 OR 카테고리 )
    public OrderAmountPricesResponseDto getMonthOrderPrices(MonthAmountRequestDto monthAmountRequestDto){
        // 날짜 형변환
        String startDateString = monthAmountRequestDto.getStartDate();
        String endDateString = monthAmountRequestDto.getEndDate();

        // 월간 조회 날짜 검증
        LocalDateTime startDateTime = validMonth(startDateString);
        LocalDateTime endDateTime = validMonthV2(startDateTime, endDateString);

        return orderRepository.getMonthOrderPrices(startDateTime, endDateTime,
                monthAmountRequestDto.getCategory(), monthAmountRequestDto.getStoreId());
    }

    // 일간 조회 입력 날짜 검증
    public LocalDateTime validDate(String dateString){
        if(dateString == null) {
            LocalDate date = LocalDate.now();
            return date.atTime(00,00,00);
        }
        LocalDate date = LocalDate.parse(dateString);
        return date.atTime(00,00,00);
    }
    // 일간 조회 입력날짜 +1일
    public LocalDateTime validDateV2(LocalDateTime startDateTime){
        return startDateTime.plusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);
    }

    //월간 조회 입력 날짜 검증
    public LocalDateTime validMonth(String startDateString){
        if(startDateString == null) {
            LocalDate startDate = LocalDate.now();
            return startDate.atTime(00,00,00);
        }
        LocalDate startDate = LocalDate.parse(startDateString);
        return startDate.atTime(00,00,00);
    }

    // 비교날짜가 null 일 경우 시작날짜 + 1개월
    public LocalDateTime validMonthV2(LocalDateTime startDateTime,String endDateString){
        if(endDateString == null) {
            return startDateTime.plusMonths(1).plusHours(23).plusMinutes(59).plusSeconds(59);
        }
        LocalDate endDate = LocalDate.parse(endDateString);
        return endDate.atTime(23,59,59);
    }




}
