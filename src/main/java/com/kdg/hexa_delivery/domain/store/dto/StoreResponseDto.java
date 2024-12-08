package com.kdg.hexa_delivery.domain.store.dto;

import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.global.enums.Status;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreResponseDto {

    private final Long storeId;

    private final Long userId;

    private final String storeName;

    private final Category category;

    private final String phone;

    private final String address;

    private final String storeDetail;

    private final Status status;

    private final String openingHour;

    private final String closingHour;

    private final Integer minimumOrderValue;

    private final List<String> imageUrls;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public StoreResponseDto(Long storeId, Long userId,
                            String storeName, Category category,
                            String phone, String address,
                            String storeDetail, Status status,
                            String openingHour, String closingHour,
                            Integer minimumOrderValue, List<String> imageUrls, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.storeId = storeId;
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.storeDetail = storeDetail;
        this.status = status;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.minimumOrderValue = minimumOrderValue;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static StoreResponseDto toDto(Store store, List<Image> images) {
        List<String> imageUrls = new ArrayList<>();

        // 업로드 이미지가 null 인 경우 디폴트 이미지로 반환
        if(images == null){
            String imageUrl = "https://hexa-test.s3.ap-southeast-2.amazonaws.com/storeImage/default.jpg";
            imageUrls.add(imageUrl);
        }
        else {
            // 이미지 url 가져오기
            for (Image image : images) {
                imageUrls.add(image.getImageUrl());
            }
        }
        return new StoreResponseDto(
                store.getStoreId(),
                store.getUser().getId(),
                store.getStoreName(),
                store.getCategory(),
                store.getPhone(),
                store.getAddress(),
                store.getStoreDetail(),
                store.getStatus(),
                store.getOpeningHours(),
                store.getClosingHours(),
                store.getMinimumOrderValue(),
                imageUrls,
                store.getCreatedAt(),
                store.getModifiedAt()
        );
    }

}
