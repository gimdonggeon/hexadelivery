package com.kdg.hexa_delivery.domain.store.dto;

import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    @NotBlank
    private final String storeName;

    @Enumerated(EnumType.STRING)
    private final Category category;

    @NotBlank
    private final String phone;

    @NotBlank
    private final String address;

    @NotBlank
    private final String storeDetail;

    @NotBlank
    private final String openingHours;

    @NotBlank
    private final String closingHours;

    private final Integer minimumOrderValue;

    public StoreRequestDto(String storeName,
                           Category category, String phone,
                           String address, String storeDetail,
                           String openingHours, String closingHours,
                           Integer minimumOrderValue) {
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
