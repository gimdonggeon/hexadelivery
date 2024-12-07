package com.kdg.hexa_delivery.domain.image.repository;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {


    List<Image> findByOwnerIdAndOwner(Long ownerId, ImageOwner owner);

    boolean deleteByImageName(String imageName);

    Image findByImageName(String imageName);
}
