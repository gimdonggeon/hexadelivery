package com.kdg.hexa_delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    @NotBlank
    private final String storeName;

    @NotBlank
    private final String category;

    @NotBlank
    private final String phone;

    @NotBlank
    private final String address;

    @NotBlank
    private final String storeDetail;

    public StoreRequestDto(String storeName,
                           String category, String phone,
                           String address, String storeDetail) {

        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
    }

}
