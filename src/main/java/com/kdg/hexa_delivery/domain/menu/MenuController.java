package com.kdg.hexa_delivery.domain.menu;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/stores/{storeId}/menus")
public class MenuController {

    MenuService menuService;

    @Autowired
    MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     *  메뉴 생성 API
     *
     * @param menuRequestDto  생성할 메뉴 정보 dto
     * @return ResponseEntity<MenuResponseDto>  저장된 메뉴 정보 전달
     *
     */
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestParam Long storeId, @RequestBody @Valid MenuRequestDto menuRequestDto) {

        //메뉴 생성
        MenuResponseDto menuResponseDto = menuService.createMenu(menuRequestDto.getMenuName(), menuRequestDto.getPrice(), storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponseDto);
    }

    /**
     *  메뉴 전체 조회 API
     *
     * @param storeId  조회할 메뉴들의 가게 id
     * @return ResponseEntity<List<MenuResponseDto>>  저장된 메뉴 정보 전달
     *
     */
    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenus(@RequestParam Long storeId) {

        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenus(storeId));
    }

    /**
     *  메뉴 생성 API
     *
     * @param menuId  조회할 메뉴들의 가게 id
     * @param updateMenuRequestDto  수정할 메뉴 정보 dto
     * @return ResponseEntity<MenuResponseDto>  수정된 메뉴 정보 전달
     *
     */
    @PatchMapping("{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId,
                                                      @RequestBody @Valid updateMenuRequestDto updateMenuRequestDto) {

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
     * @return ResponseEntity<String>  삭제 문구 전달
     *
     */
    @DeleteMapping("{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }
}
