package com.kdg.hexa_delivery.domain.advertise.controller;

import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseDeclinedRequestDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseResponseDto;
import com.kdg.hexa_delivery.domain.advertise.service.AdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins/advertises")
public class AdminAdvertiseController {

    private final AdvertiseService advertiseService;

    @Autowired
    public AdminAdvertiseController(AdvertiseService advertiseService) { this.advertiseService = advertiseService; }


    /*
     *  모든 광고신청 조회
     */

    @GetMapping
    public ResponseEntity<List<AdvertiseResponseDto>> getAllAdvertises() {
        return ResponseEntity.status(HttpStatus.OK).body(advertiseService.getAllAdvertises());
    }

    /*
     *  광고신청 수락
     */

    @PutMapping("/{advertiseId}/Accept")
    public ResponseEntity<AdvertiseResponseDto> AcceptAdvertise(@PathVariable("advertiseId") Long advertiseId){
        return ResponseEntity.status(HttpStatus.OK).body(advertiseService.advertiseAccept(advertiseId));
    }

    /*
     *  광고신청 거부
     */

    @PutMapping("/{advertiseId}/Decline")
    public ResponseEntity<Map<String, Object>> DeclineAdvertise(@PathVariable("advertiseId") Long advertiseId,
                                                                @RequestBody AdvertiseDeclinedRequestDto advertiseDeclinedRequestDto){

        Map<String, Object> declineAdvertise = advertiseService.advertiseDecline(advertiseId, advertiseDeclinedRequestDto);

        return new ResponseEntity<>(declineAdvertise, HttpStatus.OK);
    }

}
