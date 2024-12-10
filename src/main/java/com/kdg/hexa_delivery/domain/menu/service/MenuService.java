package com.kdg.hexa_delivery.domain.menu.service;

import com.kdg.hexa_delivery.domain.image.enums.ImageOwner;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.image.service.ImageService;
import com.kdg.hexa_delivery.domain.menu.dto.MenuResponseDto;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.menu.repository.MenuRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import com.kdg.hexa_delivery.global.exception.WrongAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ImageService imageService;



    @Autowired
    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository, ImageService imageService) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.imageService = imageService;
    }

    /**
     * 메뉴 생성 메서드
     *
     * @param menuName    메뉴 이름
     * @param price       메뉴 가격
     * @param description 메뉴 설명
     * @param menuImages   메뉴 이미지들
     * @param storeId     메뉴를 등록할 가게 id
     * @return MenuResponseDto  저장된 메뉴 정보 전달
     */
    @Transactional
    public MenuResponseDto createMenu(String menuName, Integer price, String description, List<MultipartFile> menuImages, Long storeId) {
        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 메뉴 객체 생성
        Menu menu = new Menu(menuName, price, description, Status.NORMAL, store);

        // 메뉴 저장
        Menu savedMenu = menuRepository.save(menu);


        // 이미지 s3 서버에 업로드 후 url 받아오기
        List<Image> imageUrls = imageService.takeImages(menuImages, savedMenu.getId(), ImageOwner.MENU);


        return MenuResponseDto.toDto(savedMenu, imageUrls);
    }

    /**
     *  메뉴 전체 조회 메서드
     *
     * @param storeId 메뉴를 조회할 가게 id
     *
     * @return List<MenuResponseDto>  가게에 있는 메뉴 전체 리스트 전달
     */
    public List<MenuResponseDto> getMenus(Long storeId) {
        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        // 해당 가게에 있는 메뉴 모두 가져오기
        List<Menu> menus =  menuRepository.findAllByStoreAndStatus(store, Status.NORMAL);

        if(menus.isEmpty()){
            throw new NotFoundException(ExceptionType.STORE_MENU_NOT_FOUND);
        }

        // 메뉴의 정보와 이미지를 ResponseDto 로 변환
        return menus.stream()
                .map(menu -> MenuResponseDto.toDto(menu, imageService.findImages(menu.getId(), ImageOwner.MENU))
                )
                .toList();
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
    public MenuResponseDto updateMenu(Long menuId, String menuName, Integer price, String description, List<MultipartFile> menuImages) {
        // 메뉴 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        // 삭제 메뉴 수정 불가
        if(menu.getStatus() != Status.NORMAL) {
            throw new WrongAccessException(ExceptionType.DELETED_MENU);
        }

        // 이미지 s3 서버에 업로드 후 url 받아오기
        List<Image> imageUrls = imageService.takeImages(menuImages, menu.getId(), ImageOwner.MENU);

        // 메뉴 정보 수정
        menu.updateMenu(menuName, price, description);

        // 메뉴 저장 - 명시
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu, imageUrls);
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

        // 메뉴 저장 - 명시
        menuRepository.save(menu);
    }


}
