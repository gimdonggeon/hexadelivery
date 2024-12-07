package com.kdg.hexa_delivery.domain.order.repository;

import com.kdg.hexa_delivery.domain.order.dto.*;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Optional<Order> findById(Long orderId);

   default Order findByIdOrElseThrow(Long orderId){
        return findById(orderId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    };
    // 일간 주문 수  :: 카테고리가 널값이거나 가게아이디가 널값인경우 둘다 널값인경우에도  동작
    @Query("SELECT new com.kdg.hexa_delivery.domain.order.dto.OrdersAmountResponseDto (COUNT(o)) FROM Order o " +
            "WHERE o.createdAt = :date AND o.orderStatus = 'ORDERED'" +
            " AND (:category IS NULL OR o.store.category = :category) AND (:storeId IS NULL OR o.store.storeId = :storeId) ")
    OrdersAmountResponseDto getDayOrders(LocalDateTime date, String category, Long storeId);

    // 일간 주문 금액  :: 카테고리가 널값이거나 가게아이디가 널값인경우 둘다 널값인경우에도  동작
    @Query("SELECT new com.kdg.hexa_delivery.domain.order.dto.OrderAmountPricesResponseDto (SUM(o.totalPrice)) FROM Order o" +
            " WHERE o.createdAt = :date AND o.orderStatus = 'ORDERED' " +
            "AND (:category IS NULL OR o.store.category = :category)" +
            "AND (:storeId IS NULL OR o.store.storeId = :storeId) ")
    OrderAmountPricesResponseDto getDayOrderPrices(LocalDateTime date, String category, Long storeId);

    // 월간 주문 수 :: 카테고리가 널값이거나 가게아이디가 널값인경우 둘다 널값인경우에도  동작
    @Query("SELECT new com.kdg.hexa_delivery.domain.order.dto.OrdersAmountResponseDto (COUNT(o)) FROM Order o" +
            " WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND (:category IS NULL OR o.store.category = :category)" +
            "AND (:storeId IS NULL OR o.store.storeId = :storeId) ")
    OrdersAmountResponseDto getMonthOrders(LocalDateTime startDate, LocalDateTime endDate,
                                           String category, Long storeId);
    // 월간 주문 금액 :: 카테고리가 널값이거나 가게아이디가 널값인경우 둘다 널값인경우에도  동작
    @Query("SELECT new com.kdg.hexa_delivery.domain.order.dto.OrderAmountPricesResponseDto (SUM(o.totalPrice)) FROM Order o" +
            " WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND (:category IS NULL OR o.store.category = :category)" +
            "AND (:storeId IS NULL OR o.store.storeId = :storeId) ")
    OrderAmountPricesResponseDto getMonthOrderPrices(LocalDateTime startDate, LocalDateTime endDate,
                                                     String category, Long storeId);
}

