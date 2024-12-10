package com.kdg.hexa_delivery.domain.store.repository;

import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사업자 가게 전체조회
    List<Store> findAllByUser_Id(Long userId);

    // 가게 단건조회
    default Store findByIdOrElseThrow(Long storeId){
        return findById(storeId).orElseThrow(() -> new NotFoundException(ExceptionType.STORE_NOT_FOUND));
    }

    // 사업자에게 등록된 사업장중 영업중인 사업장 갯수
    // @Query("SELECT COUNT(s) FROM Store s WHERE s.user.id = :userId AND s.status = 'NORMAL'")
    int countByUserIdAndStatus(Long userId, Status status);

    // SELECT COUNT(s.*) FROM store s WHERE s.user_id = 1 AND s.status = 'NORMAL'

    // select, insert, join, update, delete

    //int findAllByUserAndStatus(User user, Status status); //

    //int countByUserIdAndStatus(Long userId, Status status);

    // 검색 기준에 따른 가게정보 호출 :: 생성일자
    // @Query("SELECT s FROM Store s WHERE s.category =:category AND s.status = 'NORMAL' ORDER BY s.createdAt DESC ")
    List<Store> findAllByCategoryAndStatusOrderByCreatedAtDesc(Category category, Status status);


    // 검색 기준에 따른 가게정보 호출 :: 리뷰개수
    @Query("SELECT s FROM Store s LEFT JOIN s.reviewList r WHERE s.category =:category AND s.status = 'NORMAL' GROUP BY s ORDER BY COUNT(r) DESC ")
    List<Store> findAllOrderByReviews(Category category);

    // 검색 기준에 따른 가게정보 호출 :: 평균별점
    @Query("SELECT s FROM Store s LEFT JOIN s.reviewList r WHERE s.category =:category AND s.status = 'NORMAL' GROUP BY s ORDER BY AVG(r.rating) DESC ")
    List<Store> findAllOrderByRating(Category category);
}
