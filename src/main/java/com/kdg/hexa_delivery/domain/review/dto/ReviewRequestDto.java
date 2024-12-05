package com.kdg.hexa_delivery.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
    private Long userId;  // 리뷰 작성자의 userId
    private Integer rating;  // 평점
    private String comment;  // 댓글
}