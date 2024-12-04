package com.kdg.hexa_delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreRequestDto {

    @NotNull
    private Long userId;

    @NotBlank
    private String storeName;

    @NotBlank
    private String category;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String storeDetail;

    public StoreRequestDto(Long userId, String storeName,
                           String category, String phone,
                           String address, String storeDetail) {
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
    }

}
