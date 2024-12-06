package com.kdg.hexa_delivery.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class OrderRequestDto {

    @NotNull(message = "메뉴 이름은 필수입니다.")
    private String menuName;

    @NotNull
    @Min(value = 1, message = "수량은 최소1개입니다.")
    private Integer quantity;

    public OrderRequestDto(String menuName, Integer quantity) {
        this.menuName = menuName;
        this.quantity = quantity;
    }
}