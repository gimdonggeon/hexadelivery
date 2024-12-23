package com.kdg.hexa_delivery.domain.menu.controller;

import com.kdg.hexa_delivery.global.validation.Validation;
import com.kdg.hexa_delivery.domain.menu.dto.MenuRequestDto;
import com.kdg.hexa_delivery.domain.menu.dto.MenuResponseDto;
import com.kdg.hexa_delivery.domain.menu.dto.UpdateMenuRequestDto;
import com.kdg.hexa_delivery.domain.menu.service.MenuService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owners/stores")
public class OwnerMenuController {

    private final MenuService menuService;

    @Autowired
    public OwnerMenuController(MenuService menuService) {
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
                                                      @RequestParam(required = false) List<MultipartFile> menuImages,
                                                      @ModelAttribute @Valid MenuRequestDto menuRequestDto,
                                                      HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 권한 체크
        Validation.validMyStoreAccess(storeId, loginUser);

        //메뉴 생성
        MenuResponseDto menuResponseDto = menuService.createMenu(
                menuRequestDto.getMenuName(),
                menuRequestDto.getPrice(),
                menuRequestDto.getDescription(),
                menuImages,
                storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponseDto);
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
    @PutMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
                                                      @PathVariable Long menuId,
                                                      @RequestParam(required = false) List<MultipartFile> menuImages,
                                                      @ModelAttribute @Valid UpdateMenuRequestDto updateMenuRequestDto,
                                                      HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 권한 체크
        Validation.validMyStoreAccess(storeId, loginUser);

        // 메뉴 수정
        MenuResponseDto menuResponseDto = menuService.updateMenu(
                menuId,
                updateMenuRequestDto.getMenuName(),
                updateMenuRequestDto.getPrice(),
                updateMenuRequestDto.getDescription(),
                menuImages
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
    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long storeId,
                                             @PathVariable Long menuId,
                                             HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 권한 체크
        Validation.validMyStoreAccess(storeId, loginUser);

        // 메뉴 삭제
        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }
}
