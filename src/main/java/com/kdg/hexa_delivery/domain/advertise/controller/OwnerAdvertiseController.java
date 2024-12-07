package com.kdg.hexa_delivery.domain.advertise.controller;

import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseDeclinedResponseDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseRequestDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseResponseDto;
import com.kdg.hexa_delivery.domain.advertise.service.AdvertiseService;
import com.kdg.hexa_delivery.global.validation.Validation;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners/advertises")
public class OwnerAdvertiseController {

    private final AdvertiseService advertiseService;

    @Autowired
    public OwnerAdvertiseController(AdvertiseService advertiseService){ this.advertiseService = advertiseService; }

    /*
     *  광고 신청 API
     */
    @PostMapping("/{storeId}")
    public ResponseEntity<AdvertiseResponseDto> applyAdvertisement(@RequestBody AdvertiseRequestDto advertiseRequestDto,
                                                                   @PathVariable Long storeId,
                                                                   HttpServletRequest httpServletRequest){

        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 권한 체크
        Validation.validMyStoreAccess(storeId, loginUser);

        AdvertiseResponseDto advertiseResponseDto = advertiseService.createAdvertise(loginUser, storeId, advertiseRequestDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(advertiseResponseDto);
    }

    /*
     *  광고신청한 내 가게들 조회
     */
    @GetMapping
    public ResponseEntity<List<AdvertiseResponseDto>> advertiseMyStores(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        return ResponseEntity.status(HttpStatus.OK).body(advertiseService.getMyAdvertises(loginUser.getId()));
    }

    /*
     *  거절된 광고 사유 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<AdvertiseDeclinedResponseDto> declinedMyAdvertise(@PathVariable Long storeId,
                                                                            HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 권한 체크
        Validation.validMyStoreAccess(storeId, loginUser);

        // 거절된 광고 사유 조회
        AdvertiseDeclinedResponseDto advertiseDeclinedResponseDto = advertiseService.getDeclinedMyAdvertise(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(advertiseDeclinedResponseDto);

    }





}
