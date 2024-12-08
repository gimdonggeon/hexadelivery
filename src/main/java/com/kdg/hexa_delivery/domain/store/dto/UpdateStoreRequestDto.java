package com.kdg.hexa_delivery.domain.store.dto;

import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import lombok.Getter;

@Getter
public class UpdateStoreRequestDto {

    private final String storeName;

    private final Category category;

    private final String phone;

    private final String address;

    private final String storeDetail;

    private final String openingHours;

    private final String closingHours;

    private final Integer minimumOrderValue;

    public UpdateStoreRequestDto(String storeName, Category category, String phone, String address, String storeDetail, String openingHours, String closingHours, Integer minimumOrderValue) {
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
