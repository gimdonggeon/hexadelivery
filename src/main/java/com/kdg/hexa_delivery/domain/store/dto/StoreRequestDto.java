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

    @NotBlank
    private String openingHours;

    @NotBlank
    private String closingHours;

    private Integer minimumOrderValue;

    public StoreRequestDto(Long userId, String storeName,
                           String category, String phone,
                           String address, String storeDetail,
                           String openingHours, String closingHours,
                           Integer minimumOrderValue) {
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
        this.minimumOrderValue = minimumOrderValue;
    }

}
