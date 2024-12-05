package com.kdg.hexa_delivery.domain.review.dto;

import com.kdg.hexa_delivery.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id; // 리뷰 ID
    private Integer rating; // 평점
    private String comment; // 댓글
    private LocalDateTime reviewTime; // 리뷰 작성 시간
    private Status status; // 리뷰 상태

    // Review -> ReviewResponseDto 변환
    public static ReviewResponseDto toDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.id = review.getId();
        dto.rating = review.getRating();
        dto.comment = review.getComment();
        dto.reviewTime = review.getReviewTime();
        dto.status = review.getStatus();
        return dto;
    }
}
