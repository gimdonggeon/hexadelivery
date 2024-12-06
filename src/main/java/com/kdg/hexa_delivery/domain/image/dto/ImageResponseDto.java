package com.kdg.hexa_delivery.domain.image.dto;

import com.kdg.hexa_delivery.domain.image.entity.Image;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageResponseDto {

    private final List<String> imageList;

    public ImageResponseDto(List<String> imageList) {
        this.imageList = imageList;
    }

    public static ImageResponseDto toDto(List<Image> images){
        List<String> imageList = new ArrayList<>();

        for(Image image : images){
            imageList.add(image.getImageUrl());
        }
        return new ImageResponseDto(imageList);
    }
}
