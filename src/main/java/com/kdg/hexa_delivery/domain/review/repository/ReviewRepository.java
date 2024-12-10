package com.kdg.hexa_delivery.domain.review.repository;

import com.kdg.hexa_delivery.domain.review.entity.Review;
import com.kdg.hexa_delivery.global.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.store.storeId = :storeId AND r.store.status = 'NORMAL'  AND r.rating >= :minRate AND r.rating <= :maxRate AND r.user.id!= :userId ORDER BY r.createdAt DESC")
    List<Review> findAllByStoreIdNotMine(Long userId, Long storeId, int minRate, int maxRate);

    @Query("SELECT r FROM Review r WHERE r.store.storeId = :storeId AND r.store.status = 'NORMAL' AND r.rating >= :minRate AND r.rating <= :maxRate ORDER BY r.createdAt DESC")
    List<Review> findAllByStoreId(int minRate, int maxRate, Long storeId);

    List<Review> findAllByUserIdAndStatusOrderByCreatedAtDesc(Long user_id, Status status);
}
