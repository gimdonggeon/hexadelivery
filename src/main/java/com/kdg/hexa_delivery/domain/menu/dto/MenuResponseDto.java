package com.kdg.hexa_delivery.domain.menu.dto;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MenuResponseDto {

    private final Long menuId;

    private final Long storeId;

    private final String menuName;

    private final Integer price;

    private final List<String> imageUrls;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final Status status;

    public MenuResponseDto(Long storeId, Long menuId,
                           String menuName, Integer price, List<String> imageUrls,
                           LocalDateTime createdAt, LocalDateTime modifiedAt,
                           Status status) {

        this.storeId = storeId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.status = status;
    }

    public static MenuResponseDto toDto(Menu menu) {
        List<String> imageUrls = new ArrayList<>();

        // 업로드 이미지가 null 인 경우 디폴트 이미지로 반환
        if(menu.getImageList() == null){
            String imageUrl = "https://hexa-test.s3.ap-southeast-2.amazonaws.com/menuImage/default.jpg";
            imageUrls.add(imageUrl);
        }

        // 이미지 url 가져오기
        for (Image image : menu.getImageList()){
            imageUrls.add(image.getImageUrl());
        }

        return new MenuResponseDto(
                menu.getStore().getStoreId(),
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                imageUrls,
                menu.getCreatedAt(),
                menu.getModifiedAt(),
                menu.getStatus());

    }
}
