package com.kdg.hexa_delivery.domain.review.service;

import com.kdg.hexa_delivery.domain.base.enums.OrderStatus;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import com.kdg.hexa_delivery.domain.order.repository.OrderRepository;
import com.kdg.hexa_delivery.domain.review.dto.ReviewResponseDto;
import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.domain.review.repository.ReviewRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.kdg.hexa_delivery.domain.review.dto.ReviewResponseDto.toDto;
import static java.util.stream.Collectors.toList;

@Service
@Getter
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 리뷰 생성 메서드
     *
     * @param orderId 리뷰를 달 주문의 id
     * @param user    세션 이용자
     * @param rating  리뷰 점수
     * @param content 리뷰 내용
     * @return 리뷰 정보 dto
     */
    public ReviewResponseDto saveReview(Long orderId, User user, int rating, String content) {

        //주문 정보
        Order order = orderRepository.findByIdOrElseThrow(orderId);

        //주문이 배달완료상태가 아닐 시 예외처리
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "배달이 완료되지 않은 주문건입니다.");
        }

        if (order.getReview() != null) {
            throw new RuntimeException("리뷰가 이미 작성된 주문건입니다.");
        }

        //가게 정보
        Store store = order.getStore();

        //리뷰 생성
        Review review = new Review(user, store, order, rating, content, Status.NORMAL);

        //저장된 리뷰 정보
        Review savedReview = reviewRepository.save(review);

        return toDto(savedReview);
    }

    /**
     * 세션 사용자를 제외한 가게 리뷰 조회 메서드
     *
     * @param userId 세션 사용자의 id
     * @param storeId 가게의 id
     * @param minRate 조회할 리뷰의 최소 별점
     * @param maxRate 조회할 리뷰의 최대 별점
     *
     * @return 세션 사용자를 제외한 가게 리뷰 리스트 반환
     */
    public List<ReviewResponseDto> getAllReviewsNotMine(Long userId, Long storeId, int minRate, int maxRate) {

        // 가게 유무 확인
        if (!storeRepository.existsById(storeId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 가게가 존재하지 않습니다.");
        }

        // 리뷰 찾기
        List<Review> reviews = reviewRepository.findAllByStoreIdNotMine(userId, storeId, minRate, maxRate);

        // 리뷰 유무 확인
        if (reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰가 없습니다.");
        }

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }

    /**
     * 가게 전체 조회 메서드 - owner 조회
     *
     * @param minRate 조회할 리뷰의 최소 별점
     * @param maxRate 조회할 리뷰의 최대 별점
     * @param storeId 가게 정보
     *
     * @return 가게 전체 리뷰 조회
     */
    public List<ReviewResponseDto> getAllReviews(int minRate, int maxRate, Long storeId) {

        // 가게 유무 확인
        if (!storeRepository.existsById(storeId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 가게가 존재하지 않습니다.");
        }

        // 리뷰 찾기
        List<Review> reviews = reviewRepository.findAllByStoreId(minRate, maxRate, storeId);

        // 리뷰 유무 확인
        if (reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰가 없습니다.");
        }

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }


    /**
     * 세션 사용자의 전체 리뷰 조회 메서드
     *
     * @param user 세션 사용자
     *
     * @return 세션 사용자의 전체 리뷰 리스트 반환
     */
    public List<ReviewResponseDto> getMyReviews(User user) {

        List<Review> reviews = reviewRepository.findAllByUserId(user.getId());

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }
}
