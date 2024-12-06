package com.kdg.hexa_delivery.domain.advertise.repository;

import com.kdg.hexa_delivery.domain.advertise.entity.Advertise;
import com.kdg.hexa_delivery.domain.base.enums.AdvertiseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    // 광고신청한 내 가게들 조회
    @Query("SELECT a FROM Advertise a WHERE a.user.id = :userId")
    List<Advertise> findAllByUser_Id(Long userId);

    Advertise findByAdvertiseId(Long advertiseId);

    // 가게아이디로 광고신청 거절당한 가게 신청 조회
    @Query("SELECT a FROM Advertise a WHERE a.store.storeId = :storeId AND a.advertiseStatus = :status ")
    Advertise findByStore_StoreIdAndAdvertiseStatus_DECLINED(Long storeId, AdvertiseStatus status);
}