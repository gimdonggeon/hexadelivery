package com.kdg.hexa_delivery.domain.menu;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/stores")
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
        validMenuAccess(httpServletRequest, storeId);

        //메뉴 생성
        MenuResponseDto menuResponseDto = menuService.createMenu(
                menuRequestDto.getMenuName(),
                menuRequestDto.getPrice(),
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
    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
                                                      @PathVariable Long menuId,
                                                      @RequestBody @Valid updateMenuRequestDto updateMenuRequestDto,
                                                      HttpServletRequest httpServletRequest) {
        // 권한 체크
        validMenuAccess(httpServletRequest, storeId);

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
        validMenuAccess(httpServletRequest, storeId);

        // 메뉴 삭제
        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }


    /**
     *  메뉴 접근 권한 확인 메서드
     *
     * @param httpServletRequest  request 객체
     *
     */
    private void validMenuAccess(HttpServletRequest httpServletRequest, Long storeId) {
        HttpSession session = httpServletRequest.getSession(false);
        User LoginUser = (User) session.getAttribute("loginUser");

        // 사장님이 아니면 예외 발생
        if(LoginUser.getRole() != Role.MERCHANT){
            throw new RuntimeException("사장님이 아닙니다.");
        }
        // 사징님의 본인 가게가 아니면 예외 발생
        LoginUser.getStoreList().stream()
                .filter(store -> store.getStoreId().equals(storeId)).findAny()
                .orElseThrow(()-> new RuntimeException("본인의 가게가 아닙니다."));

    }
}
