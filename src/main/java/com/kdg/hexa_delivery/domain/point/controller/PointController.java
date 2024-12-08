package com.kdg.hexa_delivery.domain.point.controller;

import com.kdg.hexa_delivery.domain.point.dto.PointResponseDto;
import com.kdg.hexa_delivery.domain.point.dto.TotalPointResponseDto;
import com.kdg.hexa_delivery.domain.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers/points")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    /**
     *  유저 총 포인트 가져오기 API
     *
     * @param userId 유저 ID
     *
     * @return 총 포인트 전달
     */
    @GetMapping
    public ResponseEntity<TotalPointResponseDto> getTotalPoint(@RequestParam Long userId) {
        // 쿠폰 발급하기
        TotalPointResponseDto totalPointResponseDto = pointService.getMyPoint(userId);

        return ResponseEntity.ok().body(totalPointResponseDto);

    }

    /**
     * 포인트 목록 가져오기 API
     *
     * @param userId 쿠폰 ID
     *
     * @return 포인트 목록 정보 전달
     */
    @GetMapping("/list")
    public ResponseEntity<List<PointResponseDto>> getPointList(@RequestParam Long userId) {
        // 쿠폰 발급하기
        List<PointResponseDto> pointResponseDtoList = pointService.getMyPointList(userId);

        return ResponseEntity.ok().body(pointResponseDtoList);

    }
}
