package com.kdg.hexa_delivery.domain.store.entity;

import lombok.Getter;

@Getter
public class UpdateStoreRequestDto {

    private final String storeName;
    private final String category;
    private final String phone;
    private final String address;
    private final String storeDetail;

    public UpdateStoreRequestDto(String storeName, String category, String phone, String address, String storeDetail) {
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
    }
}
