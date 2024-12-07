package com.kdg.hexa_delivery.domain.menu.repository;

import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 가게 메뉴 조회시
    @Query("SELECT m FROM Menu m WHERE m.store.storeId = :storeId AND m.status = :status ")
    List<Menu> findAllByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") Status status);

    // 주문 내역 조회시에는 삭제한 메뉴도 필요
    @Query("SELECT m FROM Menu m WHERE m.store.storeId = :storeId ")
    List<Menu> findAllByStoreId(@Param("storeId") Long storeId);

    default Menu findByIdOrElseThrow(Long menuId){
        return findById(menuId).orElseThrow(()-> new NotFoundException(ExceptionType.MENU_NOT_FOUND));
    }
}
