package com.kdg.hexa_delivery.domain.shopping_basket.controller;

import com.kdg.hexa_delivery.domain.shopping_basket.dto.ShoppingBasketRequestDto;
import com.kdg.hexa_delivery.domain.shopping_basket.dto.ShoppingBasketResponseDto;
import com.kdg.hexa_delivery.domain.shopping_basket.service.ShoppingBasketService;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController("/api/shopping")
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;

    @Autowired
    ShoppingBasketController(ShoppingBasketService shoppingBasketService) {
        this.shoppingBasketService = shoppingBasketService;
    }

    /**
     *  장바구니 추가 API
     *
     * @param requestDto  생성할 장바구니 정보 dto
     * @param httpServletRequest  request 객체
     * @param httpServletResponse  response 객체
     *
     * @return ResponseEntity<String>  저장 메세지 전달 및 쿠키 전달
     *
     */
    @PostMapping("/baskets")
    public ResponseEntity<String> shoppingBasket(@RequestBody ShoppingBasketRequestDto requestDto,
                                                       HttpServletRequest httpServletRequest,
                                                       HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 쿠키 정보 셋팅
        Cookie newCookie = shoppingBasketService.addShoppingBasket(
                httpServletRequest.getCookies(),
                loginUser.getId(),
                requestDto.getStoreId(),
                requestDto.getMenuId(),
                requestDto.getQuantity()
        );

        // response 에 쿠키 추가
        httpServletResponse.addCookie(newCookie);

        return ResponseEntity.ok().body("장바구니 담기 완료했습니다.");
    }

    /**
     *  장바구니 조회 API
     *
     * @param httpServletRequest  request 객체
     *
     * @return ResponseEntity<ShoppingBasketResponseDto>  장바구니 정보 전달
     *
     */
    @GetMapping("/baskets")
    public ResponseEntity<ShoppingBasketResponseDto> getShoppingBasket(HttpServletRequest httpServletRequest) throws IOException {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 장바구니 주인 검증
        shoppingBasketService.ValidShoppingBasketOwner(loginUser, httpServletRequest.getCookies());

        // 장바구니 정보 가져오기
        ShoppingBasketResponseDto shoppingBasketResponseDto =
                shoppingBasketService.getShoppingBasket(httpServletRequest.getCookies());

        return ResponseEntity.ok().body(shoppingBasketResponseDto);
    }

    /**
     *  장바구니 수정 API
     *
     * @param requestDto  생성할 장바구니 정보 dto
     * @param httpServletRequest  request 객체
     * @param httpServletResponse  response 객체
     *
     * @return ResponseEntity<String>  저장 메세지 전달 및 쿠키 전달
     *
     */
    @PatchMapping("/baskets")
    public ResponseEntity<String> updateShoppingBasket(@RequestBody ShoppingBasketRequestDto requestDto,
                                                 HttpServletRequest httpServletRequest,
                                                 HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 장바구니 주인 검증
        shoppingBasketService.ValidShoppingBasketOwner(loginUser, httpServletRequest.getCookies());

        // 쿠키 정보 셋팅
        Cookie newCookie = shoppingBasketService.updateShoppingBasket(
                loginUser.getId(),
                httpServletRequest.getCookies(),
                requestDto.getStoreId(),
                requestDto.getMenuId(),
                requestDto.getQuantity()
        );

        // response 에 쿠키 추가
        httpServletResponse.addCookie(newCookie);

        return ResponseEntity.ok().body("장바구니 수정을 완료했습니다.");
    }


    /**
     *  장바구니 삭제 API
     *
     * @param httpServletResponse  response 객체
     *
     * @return ResponseEntity<String>  삭제 메세지 전달
     *
     */
    @DeleteMapping("/baskets")
    public ResponseEntity<String> deleteShoppingBasket(HttpServletRequest httpServletRequest,
                                                       HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 장바구니 주인 검증
        shoppingBasketService.ValidShoppingBasketOwner(loginUser, httpServletRequest.getCookies());

        // 장바구니 정보 null 로 초기화
        Cookie newCookie = new Cookie("shoppingBasket", null);

        // response 에 쿠키 추가
        httpServletResponse.addCookie(newCookie);

        return ResponseEntity.ok().body("장바구니 제거 완료했습니다.");
    }


}

