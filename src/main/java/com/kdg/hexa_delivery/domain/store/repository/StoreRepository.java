package com.kdg.hexa_delivery.domain.store.repository;

import com.kdg.hexa_delivery.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 가게 전체조회
    List<Store> findAllByUser_UserId(Long userId);
    
    // 가게 단건조회
    default Store findByIdOrElseThrow(Long storeId){
        return findById(storeId).orElseThrow(() -> new RuntimeException("해당 id의 가게를 찾을 수 없습니다."));
    };

    // 사업자에게 등록된 사업장중 영업중인 사업장 갯수
    @Query("SELECT COUNT(s) FROM Store s WHERE s.user.user_id = :userId AND s.closure = 'OPEN'")
    int findAllByUser_UserIdAndClosureOpen(@Param("userId") Long userId);
}
