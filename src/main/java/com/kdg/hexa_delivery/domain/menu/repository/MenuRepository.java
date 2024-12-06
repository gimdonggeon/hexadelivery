package com.kdg.hexa_delivery.domain.menu.repository;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 가게 메뉴 조회시
    @Query("SELECT m FROM Menu m WHERE m.store.storeId = :storeId AND m.status = :status ")
    List<Menu> findAllByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") Status status);

    // 주문 내역 조회시에는 삭제한 메뉴도 필요
    @Query("SELECT m FROM Menu m WHERE m.store.storeId = :storeId ")
    List<Menu> findAllByStoreId(@Param("storeId") Long storeId);

    default Menu findByIdOrElseThrow(Long menuId){
        return findById(menuId).orElseThrow(()-> new RuntimeException("해당 id의 메뉴를 찾을 수 없습니다."));
    }

    Optional<Menu> findByName(String menuName);

    default Menu findByNameOrElseThrow(String menuName) {
        return findByName(menuName).orElseThrow(()-> new RuntimeException("해당 id의 메뉴를 찾을 수 없습니다."));
    }
}
