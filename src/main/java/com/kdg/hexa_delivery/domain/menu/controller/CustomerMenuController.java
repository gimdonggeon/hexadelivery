package com.kdg.hexa_delivery.domain.menu.controller;

import com.kdg.hexa_delivery.domain.menu.service.MenuService;
import com.kdg.hexa_delivery.domain.menu.dto.MenuResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/customers/stores-menus")
public class CustomerMenuController {

    MenuService menuService;

    @Autowired
    CustomerMenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    /**
     *  메뉴 전체 조회 API
     *
     * @param storeId  조회할 메뉴들의 가게 id
     * @return ResponseEntity<List<MenuResponseDto>>  저장된 메뉴 정보 전달
     *
     */
    @GetMapping("/{storeId}/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {

        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenus(storeId));
    }


}
