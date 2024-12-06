package com.kdg.hexa_delivery.domain.image.service;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(S3Client s3Client, ImageRepository imageRepository) {
        this.s3Client = s3Client;
        this.imageRepository = imageRepository;
    }

    /**
     * 이미지 업로드 메서드
     *
     * @param image  저장할 이미지 파일
     * @param ownerId   저장하는 주체의 id
     * @param owner    저장하는 클래스 타입 enum
     */
    public Image uploadImage(MultipartFile image, Long ownerId, ImageOwner owner) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(image.getContentType())
                .build();

        // S3에 파일 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

        // S3 서버 객체 URL 가져오기
        String imageUrl = getPublicUrl(fileName);

        // 이미지 객체 생성
        Image imageEntity = new Image(fileName, imageUrl, ownerId, owner);

        return imageRepository.save(imageEntity);
    }

    /**
     * 이미지 조회 메서드
     *
     * @param ownerId 이미지 주인의 id
     * @param owner   이미지 주인의 클래스 타입 enum
     */
    public List<Image> findImages(Long ownerId, ImageOwner owner) {
        List<Image> images = imageRepository.findByOwnerIdAndOwner(ownerId, owner);

        if (images.isEmpty()) {
            throw new RuntimeException("이미지가 없습니다.");
        }

        return images;
    }

    /**
     * 이미지 수정 메서드
     *
     * @param image     수정할 이미지 파일
     * @param imageName 기존 이미지의 파일 이름
     */
    @Transactional
    public Image modifyImage(MultipartFile image, String imageName) throws IOException {
        Image imageEntity = imageRepository.findByImageName(imageName);

        if (imageEntity == null) {
            throw new RuntimeException("이미지 정보가 없습니다.");
        }

        // 새 파일 업로드
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(image.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

        // 기존 파일 삭제
        deleteImageFromS3(imageName);

        // S3 서버 객체 URL 가져오기
        String imageUrl = getPublicUrl(fileName);

        // 이미지 정보 업데이트
        imageEntity.updateImage(fileName, imageUrl);

        return imageRepository.save(imageEntity);
    }

    /**
     * 이미지 삭제 메서드
     *
     * @param imageName 삭제할 이미지의 이름
     */
    public void deleteImage(String imageName) {
        if (!imageRepository.deleteByImageName(imageName)) {
            throw new RuntimeException("이미지 정보가 삭제되지 못했습니다.");
        }

        deleteImageFromS3(imageName);
    }

    // S3에서 이미지 삭제 메서드
    private void deleteImageFromS3(String imageName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(imageName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    // S3 서버 경로 가져오는 메서드
    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, fileName);
    }

    // 이미지 업로드 연결 메서드
    public List<Image> takeImages(List<MultipartFile> images, Long ownerId, ImageOwner owner) {
        List<Image> imageUrls = new ArrayList<>();

        try {
            for (MultipartFile menuImage : images) {
                imageUrls.add(uploadImage(menuImage, ownerId, owner));
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류 발생", e);
        }

        return imageUrls;
    }
}
