package com.kdg.hexa_delivery.domain.menu.repository;

import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 가게 메뉴 조회시
    //@Query("SELECT m FROM Menu m WHERE m.store.storeId = :storeId AND m.status = :status ")
    List<Menu> findAllByStoreAndStatus(Store store, Status status);

    default Menu findByIdOrElseThrow(Long menuId){
        return findById(menuId).orElseThrow(()-> new NotFoundException(ExceptionType.MENU_NOT_FOUND));
    }
}
