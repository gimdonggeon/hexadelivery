package com.kdg.hexa_delivery.domain.store.repository;

import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사업자 가게 전체조회
    List<Store> findAllByUser_Id(Long userId);

    // 폐점이 아닌 가게 전체 조회
    @Query("SELECT s FROM Store s WHERE s.status = 'NORMAL'")
    List<Store> findAllByStatusNORMAL();
    // 가게 단건조회
    default Store findByIdOrElseThrow(Long storeId){
        return findById(storeId).orElseThrow(() -> new NotFoundException(ExceptionType.STORE_NOT_FOUND));
    }

    // 사업자에게 등록된 사업장중 영업중인 사업장 갯수
    @Query("SELECT COUNT(s) FROM Store s WHERE s.user.id = :userId AND s.status = 'NORMAL'")
    int findAllByUser_UserIdAndStatusNORMAL(@Param("userId") Long userId);
}
