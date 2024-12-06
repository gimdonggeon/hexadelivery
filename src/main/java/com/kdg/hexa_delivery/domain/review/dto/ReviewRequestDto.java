package com.kdg.hexa_delivery.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {

    @NotNull
    @Min(value = 1, message = "최소 1점부터 줄 수 있습니다.")
    @Max(value = 5, message = "최대 5점까지 줄 수 있습니다.")
    private int rating;

    private String content;

}
