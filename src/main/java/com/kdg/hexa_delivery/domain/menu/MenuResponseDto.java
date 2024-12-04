package com.kdg.hexa_delivery.domain.menu;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuResponseDto {

    private final Long menuId;

    private final Long storeId;

    private final String menuName;

    private final Integer price;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final Status status;

    public MenuResponseDto(Long storeId, Long menuId,
                           String menuName, Integer price,
                           LocalDateTime createdAt, LocalDateTime modifiedAt,
                           Status status) {

        this.storeId = storeId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.status = status;
    }

    public static MenuResponseDto toDto(Menu menu) {

        return new MenuResponseDto(
                menu.getId(),
                menu.getStore().getStoreId(),
                menu.getName(),
                menu.getPrice(),
                menu.getCreatedAt(),
                menu.getModifiedAt(),
                menu.getStatus()
        );

    }
}
