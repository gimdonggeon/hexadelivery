package com.kdg.hexa_delivery.domain.menu;

import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
    }

    /**
     *  메뉴 생성 메서드
     *
     * @param menuName  메뉴 이름
     * @param price 메뉴 가격
     * @param storeId 메뉴를 등록할 가게 id
     *
     * @return MenuResponseDto  저장된 메뉴 정보 전달
     */
    public MenuResponseDto createMenu(String menuName, Integer price, Long storeId) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 메뉴 객체 생성
        Menu menu = new Menu(menuName, price, Status.NORMAL, store);

        // 메뉴 저장
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.toDto(savedMenu);
    }

    /**
     *  메뉴 전체 조회 메서드
     *
     * @param storeId 메뉴를 조회할 가게 id
     *
     * @return List<MenuResponseDto>  가게에 있는 메뉴 전체 리스트 전달
     */
    public List<MenuResponseDto> getMenus(Long storeId) {
        // 해당 가게에 있는 메뉴 모두 가져오기
        return menuRepository.findAllByStoreIdAndStatusNormal(storeId).stream().map(MenuResponseDto::toDto).toList();
    }

    /**
     *  메뉴 수정 메서드
     *
     * @param menuId 메뉴 id
     * @param menuName  메뉴 이름
     * @param price 메뉴 가격
     *
     * @return MenuResponseDto  수정된 메뉴 정보 전달
     */
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, String menuName, Integer price) {
        // 메뉴 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        // 메뉴 정보 수정
        menu.updateMenu(menuName, price);

        return MenuResponseDto.toDto(menu);
    }

    /**
     *  메뉴 삭제 메서드
     *  - 논리적인 삭제 이용
     *
     * @param menuId 메뉴 id
     *
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        // 메뉴 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        // 메뉴 논리적인 삭제
        menu.updateStatus2Delete();
    }
}
