package com.kdg.hexa_delivery.domain.review.controller;

import com.kdg.hexa_delivery.domain.review.dto.ReviewRequestDto;
import com.kdg.hexa_delivery.domain.review.dto.ReviewResponseDto;
import com.kdg.hexa_delivery.domain.review.service.ReviewService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerReviewController {

    private final ReviewService reviewService;

    @Autowired
    public CustomerReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 생성 API
     *
     * @param orderId            리뷰를 달 주문 id
     * @param requestDto         생성할 리뷰 정보 dto
     * @param httpServletRequest 현재 세션 정보
     * @return ResponseEntity<ReviewResponseDto> 저장된 리뷰 정보 전달
     */
    @PostMapping("orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long orderId,
                                                          @Valid @RequestBody ReviewRequestDto requestDto,
                                                          HttpServletRequest httpServletRequest) {

        //세션 이용자 정보
        HttpSession session = httpServletRequest.getSession();
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        //리뷰 저장
        ReviewResponseDto reviewResponseDto = reviewService.saveReview(orderId, loginUser, requestDto.getRating(), requestDto.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponseDto);
    }

    /**
     * 가게 전체 리뷰 조회 API
     *
     * @param storeId 가게 정보
     * @param minRate 조회할 리뷰의 최소 별점
     * @param maxRate 조회할 리뷰의 최대 별점
     * @param httpServletRequest 세션 정보
     *
     * @return 세션이용자가 작성한 리뷰를 제외한 가게의 전체 리뷰 리스트
     */
    @GetMapping("store/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(@PathVariable Long storeId,
                                                                 @RequestParam(required = false, defaultValue = "1") int minRate,
                                                                 @RequestParam(required = false, defaultValue = "5") int maxRate,
                                                                 HttpServletRequest httpServletRequest) {

        // 세션 이용자의 정보
        User user = (User) httpServletRequest.getSession(false).getAttribute(Const.LOGIN_USER);

        // 세션 사용자의 리뷰를 제외한 전체 리뷰 조회
        List<ReviewResponseDto> allReviews = reviewService.getAllReviewsNotMine(user.getId(), storeId, minRate, maxRate);

        return ResponseEntity.status(HttpStatus.OK).body(allReviews);
    }

    /**
     * 나의 리뷰 전체 조회 API
     *
     * @param httpServletRequest 세션 정보
     *
     * @return 세션이용자가 작성한 모든 리뷰 전체 조회
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(HttpServletRequest httpServletRequest) {

        //세션 이용자의 정보
        User user = (User) httpServletRequest.getSession().getAttribute(Const.LOGIN_USER);

        //세션 이용자의 리뷰 전체 조회
        List<ReviewResponseDto> myReviews = reviewService.getMyReviews(user);

        return ResponseEntity.status(HttpStatus.OK).body(myReviews);
    }
}
