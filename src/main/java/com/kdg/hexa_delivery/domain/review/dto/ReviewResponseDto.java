package com.kdg.hexa_delivery.domain.review.dto;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long reviewId;

    private Long orderId;

    private String name;

    private int rating;

    private String content;

    private LocalDateTime createdAt;

    private Status status;

    public ReviewResponseDto(Long reviewId,
                             Long orderId,
                             String name,
                             int rating,
                             String content,
                             LocalDateTime createdAt,
                             Status status
    ) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.name = name;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static ReviewResponseDto toDto(Review review) {

        return new ReviewResponseDto(
                review.getId(),
                review.getOrder().getId(),
                review.getUser().getName(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getStatus()
        );
    }
}
