package com.kdg.hexa_delivery.domain.image.repository;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {


    List<Image> findByOwnerIdAndOwner(Long ownerId, ImageOwner owner);

    boolean deleteByImageName(String imageName);

    Image findByImageName(String imageName);
}
