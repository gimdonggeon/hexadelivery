package com.kdg.hexa_delivery.domain.store.controller;

import com.kdg.hexa_delivery.domain.base.validation.Validation;
import com.kdg.hexa_delivery.domain.store.dto.StoreRequestDto;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.store.service.StoreService;
import com.kdg.hexa_delivery.domain.store.dto.UpdateStoreRequestDto;
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
public class OwnerStoreController {

    StoreService storeService;

    @Autowired
    OwnerStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /*
     * 가게 등록 API
     */
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@ModelAttribute @Valid StoreRequestDto storeRequestDto,
                                                        @RequestParam(required = false) List<MultipartFile> storeImages,
                                                        HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        StoreResponseDto storeResponseDto = storeService.createStore(loginUser, storeRequestDto, storeImages);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponseDto);
    }

    /*
     *   내 가게 전체 조회
     */
    @GetMapping("/me")
    public ResponseEntity<List<StoreResponseDto>> getMyStores(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        return ResponseEntity.status(HttpStatus.OK).body(storeService.getMyStores(loginUser.getId()));
    }

    /*
     *   가게 단건 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getOwnersStore(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(storeId));
    }

    /*
     *   가게 수정
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId,
                                                        @RequestParam(required = false) List<MultipartFile> storeImages,
                                                        @ModelAttribute @Valid UpdateStoreRequestDto updateStoreRequestDto,
                                                        HttpServletRequest httpServletRequest) {
        // 가게 접근권한 확인 메서드
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        //본인가게 확인 메서드
        Validation.validMyStoreAccess(storeId, loginUser);

        StoreResponseDto storeResponseDto = storeService.updateStore(
                storeId,
                updateStoreRequestDto.getStoreName(),
                updateStoreRequestDto.getCategory(),
                updateStoreRequestDto.getPhone(),
                updateStoreRequestDto.getAddress(),
                updateStoreRequestDto.getStoreDetail(),
                updateStoreRequestDto.getOpeningHours(),
                updateStoreRequestDto.getClosingHours(),
                updateStoreRequestDto.getMinimumOrderValue(),
                storeImages
        );
        return ResponseEntity.status(HttpStatus.OK).body(storeResponseDto);
    }


    /*
     *   가게 폐업
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId,
                                              HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        //본인가게 확인 메서드
        Validation.validMyStoreAccess(storeId, loginUser);

        storeService.deleteStore(storeId);

        return ResponseEntity.status(HttpStatus.OK).body("가게가 폐업처리 되었습니다.");
    }


}
