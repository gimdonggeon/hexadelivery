package com.kdg.hexa_delivery.domain.review.controller;

import com.kdg.hexa_delivery.domain.review.dto.ReviewResponseDto;
import com.kdg.hexa_delivery.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerReviewController {

    private final ReviewService reviewService;

    @Autowired
    public OwnerReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 가게 전체 리뷰 조회 API
     *
     * @param storeId 가게 id
     *
     * @return 리뷰 리스트 전달
     */
    @GetMapping("stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(@PathVariable Long storeId,
                                                                 @RequestParam(required = false, defaultValue = "1") int minRate,
                                                                 @RequestParam(required = false, defaultValue = "5") int maxRate) {

        List<ReviewResponseDto> allReviews = reviewService.getAllReviews(minRate, maxRate, storeId);

        return ResponseEntity.status(HttpStatus.OK).body(allReviews);
    }

}
