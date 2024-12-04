package com.kdg.hexa_delivery.domain.store.entity;

import com.kdg.hexa_delivery.domain.base.enums.Closure;
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

    private final Closure closure;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public StoreResponseDto(Long storeId,Long userId,
                            String storeName, String category,
                            String phone, String address,
                            String storeDetail, Closure closure,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.storeId = storeId;
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.closure = closure;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static StoreResponseDto toDto(Store store) {
        return new StoreResponseDto(
                store.getStoreId(),
                store.getUser().getUserId(),
                store.getStoreName(),
                store.getCategory(),
                store.getPhone(),
                store.getAddress(),
                store.getStoreDetail(),
                store.getClosure(),
                store.getCreatedAt(),
                store.getModifiedAt()
        );
    }

}
