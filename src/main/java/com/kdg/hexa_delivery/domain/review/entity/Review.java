package com.kdg.hexa_delivery.domain.review.entity;

import com.kdg.hexa_delivery.domain.base.entity.BaseEntity;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // Order와 ManyToOne 관계 설정

    @Column(nullable = false)
    private Integer rating;  // 평점

    @Column(nullable = false)
    private String comment;  // 댓글

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status reviewStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewTime;

    @Column(nullable = true)
    private LocalDateTime reviewModifiedTime;

    public Review() {
    }

    public Review(Order order, Integer rating, String comment) {
        this.order = order;
        this.rating = rating;
        this.comment = comment;
        this.reviewStatus = Status.NORMAL;
        this.reviewTime = LocalDateTime.now();
    }

    // 리뷰 수정 메서드
    public void updateReview(Integer rating, String comment) {
        if (rating != null) {
            this.rating = rating;
        }
        if (comment != null && !comment.isEmpty()) {
            this.comment = comment;
        }
        this.reviewModifiedTime = LocalDateTime.now();
    }

    // 리뷰 상태 변경
    public void updateStatusToDeleted() {
        this.reviewStatus = Status.DELETED;
    }
}
