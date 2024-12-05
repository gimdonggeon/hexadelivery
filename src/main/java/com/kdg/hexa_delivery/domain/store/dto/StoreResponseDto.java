package com.kdg.hexa_delivery.domain.store.dto;

import com.kdg.hexa_delivery.domain.base.enums.State;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class StoreResponseDto {

    private final Long storeId;

    private final Long userId;

    private final String storeName;

    private final String category;

    private final String phone;

    private final String address;

    private final String storeDetail;

    private final State state;

    private final String openingHour;

    private final String closingHour;

    private final Integer minimumOrderValue;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public StoreResponseDto(Long storeId,Long userId,
                            String storeName, String category,
                            String phone, String address,
                            String storeDetail, State state,
                            String openingHour, String closingHour,
                            Integer minimumOrderValue, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.storeId = storeId;
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.state = state;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.minimumOrderValue = minimumOrderValue;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static StoreResponseDto toDto(Store store) {
        return new StoreResponseDto(
                store.getStoreId(),
                store.getUser().getId(),
                store.getStoreName(),
                store.getCategory(),
                store.getPhone(),
                store.getAddress(),
                store.getStoreDetail(),
                store.getState(),
                store.getOpeningHours(),
                store.getClosingHours(),
                store.getMinimumOrderValue(),
                store.getCreatedAt(),
                store.getModifiedAt()
        );
    }

}
