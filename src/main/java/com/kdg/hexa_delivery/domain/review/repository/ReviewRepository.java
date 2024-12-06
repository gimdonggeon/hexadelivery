package com.kdg.hexa_delivery.domain.review.repository;

import com.kdg.hexa_delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.store.storeId = :storeId AND r.store.status = 'NORMAL'  AND r.rating >= :minRate AND r.rating <= :maxRate AND r.user.id!= :userId ORDER BY r.createdAt DESC")
    List<Review> findAllByStoreIdNotMine(Long userId, Long storeId, int minRate, int maxRate);

    @Query("SELECT r FROM Review r WHERE r.store.storeId = :storeId AND r.store.status = 'NORMAL' ORDER BY r.createdAt DESC")
    List<Review> findAllByStoreId(Long storeId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.user.status = 'NORMAL' ORDER BY r.createdAt DESC")
    List<Review> findAllByUserId(Long userId);
}
