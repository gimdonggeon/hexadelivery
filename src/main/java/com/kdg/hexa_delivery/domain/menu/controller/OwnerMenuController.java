package com.kdg.hexa_delivery.domain.menu.controller;

import com.kdg.hexa_delivery.domain.base.validation.Validation;
import com.kdg.hexa_delivery.domain.menu.dto.MenuRequestDto;
import com.kdg.hexa_delivery.domain.menu.dto.MenuResponseDto;
import com.kdg.hexa_delivery.domain.menu.dto.updateMenuRequestDto;
import com.kdg.hexa_delivery.domain.menu.service.MenuService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/owners/stores")
public class OwnerMenuController {
    MenuService menuService;

    @Autowired
    OwnerMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     *  메뉴 생성 API
     *
     * @param menuRequestDto  생성할 메뉴 정보 dto
     * @param httpServletRequest  request 객체
     *
     * @return ResponseEntity<MenuResponseDto>  저장된 메뉴 정보 전달
     *
     */
    @PostMapping("/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                                      @RequestBody @Valid MenuRequestDto menuRequestDto,
                                                      HttpServletRequest httpServletRequest) {

        // 권한 체크
        User user = Validation.validStoreAccess(httpServletRequest);
        Validation.validMyStoreAccess(storeId, user);

        //메뉴 생성
        MenuResponseDto menuResponseDto = menuService.createMenu(
                menuRequestDto.getMenuName(),
                menuRequestDto.getPrice(),
                storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponseDto);
    }

    /**
     *  메뉴 수정 API
     *
     * @param menuId  조회할 메뉴들의 가게 id
     * @param updateMenuRequestDto  수정할 메뉴 정보 dto
     * @param httpServletRequest  request 객체
     *
     * @return ResponseEntity<MenuResponseDto>  수정된 메뉴 정보 전달
     *
     */
    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
                                                      @PathVariable Long menuId,
                                                      @RequestBody @Valid updateMenuRequestDto updateMenuRequestDto,
                                                      HttpServletRequest httpServletRequest) {
        // 권한 체크
        User user = Validation.validStoreAccess(httpServletRequest);
        Validation.validMyStoreAccess(storeId, user);

        // 메뉴 수정
        MenuResponseDto menuResponseDto = menuService.updateMenu(
                menuId,
                updateMenuRequestDto.getMenuName(),
                updateMenuRequestDto.getPrice()
        );

        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDto);
    }

    /**
     *  메뉴 삭제 API
     *
     * @param menuId  조회할 메뉴들의 가게 id
     * @param httpServletRequest  request 객체
     *
     * @return ResponseEntity<String>  삭제 문구 전달
     *
     */
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long storeId,
                                             @PathVariable Long menuId,
                                             HttpServletRequest httpServletRequest) {
        // 권한 체크
        User user = Validation.validStoreAccess(httpServletRequest);
        Validation.validMyStoreAccess(storeId, user);

        // 메뉴 삭제
        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }
}
