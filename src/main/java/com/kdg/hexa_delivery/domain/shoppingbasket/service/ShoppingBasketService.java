package com.kdg.hexa_delivery.domain.shoppingbasket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.image.service.ImageService;
import com.kdg.hexa_delivery.domain.menu.entity.Menu;
import com.kdg.hexa_delivery.domain.menu.repository.MenuRepository;
import com.kdg.hexa_delivery.domain.shoppingbasket.dto.ShoppingBasketResponseDto;
import com.kdg.hexa_delivery.domain.shoppingbasket.entity.ShoppingBasket;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ShoppingBasketService {

    private final ObjectMapper objectMapper;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ImageService imageService;

    @Autowired
    public ShoppingBasketService(ObjectMapper objectMapper,
                                 StoreRepository storeRepository,
                                 MenuRepository menuRepository, ImageService imageService) {

        this.objectMapper = objectMapper;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.imageService = imageService;
    }

    /**
     * 장바구니 추가 메서드
     *
     * @param loginUserId 로그인된 유저 정보
     * @param storeId  가게 Id
     * @param menuId 저장할 메뉴 정보
     * @param quantity 한 메뉴의 개수
     *
     * @return Cookie 새로 생성한 쿠키 전달
     */
    public Cookie addShoppingBasket(Cookie[] cookies, Long loginUserId, Long storeId, Long menuId, Integer quantity) throws IOException {
        // 쿠키 가져오기
        ShoppingBasket shoppingBasket = getCookieValue(cookies);

        Map<Long, Integer> confirmedMenuList = new HashMap<>();

        // 기존에 있던 장바구니 담기
        if(shoppingBasket != null && !shoppingBasket.getMenuList().containsKey(menuId)) {
            confirmedMenuList = new HashMap<>(shoppingBasket.getMenuList());
        }

        // 기존에 있으면 개수만 추가하기
        if(confirmedMenuList.containsKey(menuId)) {
            confirmedMenuList.put(menuId, confirmedMenuList.get(menuId) + quantity);
        }
        else{
            // 실제 가게의 메뉴 정보 리스트 가져오기
            List<Menu> menus = menuRepository.findAllByStoreIdAndStatus(storeId, Status.NORMAL);

            // 실제 메뉴에 있는 음식인지 확인
            for (Menu menu : menus) {
                if(menu.getId().equals(menuId)){
                    confirmedMenuList.put(menuId, quantity);
                    break;
                }
            }
        }

        // 쿠키 만들어서 전달
        return makeCookie(loginUserId, storeId, confirmedMenuList);
    }


    /**
     * 장바구니 정보 조회 메서드
     *
     * @param cookies  request 에 담긴 쿠키들
     *
     * @return ShoppingBasketResponseDto 장바구니 정보 전달
     *
     */
    public ShoppingBasketResponseDto getShoppingBasket(Cookie[] cookies) throws IOException {
        // 쿠키 가져오기
        ShoppingBasket shoppingBasket = getCookieValue(cookies);

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(shoppingBasket.getStoreId());
        // 메뉴 정보 가져오기
        List<Menu> menus = menuRepository.findAllByStoreIdAndStatus(store.getStoreId(), Status.NORMAL);

        // 보여줄 메뉴 맵
        Map<String, Integer> menuList = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();
        Integer totalPrice = 0;

        // 실제 메뉴에 있는 음식인지 확인
        for(Menu menu : menus){
            if(shoppingBasket.getMenuList().containsKey(menu.getId())){
                menuList.put(menu.getName(), shoppingBasket.getMenuList().get(menu.getId()));
                // 대표 이미지 url 저장
                imageUrls.add(imageService.findImages(menu.getId(), ImageOwner.MENU).get(0).getImageUrl());
                totalPrice += menu.getPrice();
            }
        }

        return new ShoppingBasketResponseDto(store.getStoreName(), menuList, imageUrls, totalPrice);
    }

    /**
     * 장바구니 정보 수정 메서드
     *
     * @param loginUserId 로그인된 유저 정보
     * @param storeId  가게 Id
     * @param menuId 저장할 메뉴 정보
     * @param quantity 한 메뉴의 개수
     */
    public Cookie updateShoppingBasket(Long loginUserId, Cookie[] cookies, Long storeId, Long menuId, Integer quantity) throws IOException {
        // 쿠키 가져오기
        ShoppingBasket shoppingBasket = getCookieValue(cookies);

        // 기존 장바구니 정보 담기
        Map<Long, Integer> confirmedMenuList = new HashMap<>(shoppingBasket.getMenuList());

        // 기존에 없으면 추가
        if(!confirmedMenuList.containsKey(menuId)) {
            addShoppingBasket(cookies, loginUserId, storeId, menuId, quantity);
        }

        // 장바구니 메뉴 수정
        if(quantity > 0) {
            confirmedMenuList.put(menuId, quantity);
        }
        // 장바구니 메뉴 개수 빼기
        else if(quantity < 0) {
            confirmedMenuList.put(menuId, confirmedMenuList.get(menuId) + quantity);
        }
        // 장바구니 메뉴 삭제
        else {
            confirmedMenuList.remove(menuId);
        }

        // 쿠키 만들어서 전달
        return makeCookie(loginUserId, storeId, confirmedMenuList);
    }



    // 쿠키 생성 메서드
    public Cookie makeCookie(Long loginUserId, Long storeId, Map<Long, Integer> ConfirmedMenuList) throws IOException {
        // 장바구니 객체 생성
        ShoppingBasket newShoppingBasket = new ShoppingBasket(loginUserId, storeId, ConfirmedMenuList);

        // 객체 -> JSON 매핑
        String shoppingBasketJson = objectMapper.writeValueAsString(newShoppingBasket);
        String encodedShoppingBasketJson = URLEncoder.encode(shoppingBasketJson, StandardCharsets.UTF_8);

        // 쿠키 새로 생성
        Cookie newCookie = new Cookie("shoppingBasket", encodedShoppingBasketJson);

        // 쿠키 옵션 설정
        newCookie.setMaxAge(60*60*24);  // 24시간
        newCookie.setPath("/");
        newCookie.setSecure(true);
        newCookie.setHttpOnly(true);

        return newCookie;
    }


    // 장바구니 주인 검증 메서드
    public void ValidShoppingBasketOwner(User loginUser, Cookie[] cookies) throws IOException {
        ShoppingBasket shoppingBasket = getCookieValue(cookies);

        if(shoppingBasket == null){
            throw new RuntimeException("장바구니가 비어있습니다!");
        }

        if(!shoppingBasket.getUserId().equals(loginUser.getId())){
            throw new RuntimeException("다른 유저의 장바구니에 접근하고 있습니다.");
        }
    }


    // 쿠키의 value 가져오는 메서드
    public ShoppingBasket getCookieValue(Cookie[] cookies) throws IOException {
        if(cookies == null){
            return null;
        }

        // 쿠키 가져오기
        Cookie shoppingBasketCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("shoppingBasket")).findAny().orElse(null);

        // 쿠키 없으면
        if(shoppingBasketCookie == null){
            return null;
        }

        String decodedCookieValue = URLDecoder.decode(shoppingBasketCookie.getValue(), StandardCharsets.UTF_8);
        return objectMapper.readValue(decodedCookieValue, ShoppingBasket.class);

    }
}
