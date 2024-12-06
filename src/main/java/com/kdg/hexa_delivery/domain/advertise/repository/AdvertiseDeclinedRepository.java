package com.kdg.hexa_delivery.domain.advertise.repository;

import com.kdg.hexa_delivery.domain.advertise.entity.AdvertiseDeclined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertiseDeclinedRepository extends JpaRepository<AdvertiseDeclined, Long> {

    default AdvertiseDeclined findByAdvertise_advertiseIdOrElseThrow(Long storeId){
        return findById(storeId).orElseThrow(() -> new RuntimeException("해당 ID의 정보를 찾을 수 없습니다."));
    }
}
