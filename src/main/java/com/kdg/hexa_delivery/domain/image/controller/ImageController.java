package com.kdg.hexa_delivery.domain.image.controller;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.image.dto.ImageRequestDto;
import com.kdg.hexa_delivery.domain.image.dto.ImageResponseDto;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile,
                                              @ModelAttribute ImageRequestDto imageRequestDto) {
        try {
            Image image = imageService.uploadImage(imageFile, imageRequestDto.getOwnerId(), imageRequestDto.getOwner());
            return ResponseEntity.ok().body(image.getImageUrl());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미지 저장 실패!");
        }
    }

    @GetMapping
    public ResponseEntity<ImageResponseDto> getImages(@RequestParam Long ownerId, @RequestParam ImageOwner owner) {
        try {
            // 이미지 가져오기
            List<Image> images = imageService.findImages(ownerId, owner);

            // 이미지 보내기
            return ResponseEntity.ok().body(ImageResponseDto.toDto(images));
        } catch (Exception e) {
            throw new RuntimeException("이미지 조회 실패!");
        }
    }


    @PutMapping
    public ResponseEntity<String> modifyImage(@RequestParam("imageFile") MultipartFile imageFile,
                                              @RequestParam String imageName,
                                              @ModelAttribute ImageRequestDto imageRequestDto) {
        try {
            Image image = imageService.modifyImage(imageFile, imageName);
            return ResponseEntity.ok().body(image.getImageUrl());
        } catch (Exception e) {
            throw new RuntimeException("이미지 수정 실패!");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteImage(@RequestParam String imageUrl) {
        // 이미지 삭제
        imageService.deleteImage(imageUrl);

        return ResponseEntity.ok().body("이미지 삭제 성공!");
    }
}