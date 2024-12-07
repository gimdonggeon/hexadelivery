package com.kdg.hexa_delivery.domain.store.controller;

import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import com.kdg.hexa_delivery.domain.advertise.enums.SearchConditions;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers/stores")
public class CustomerStoreController {

    StoreService storeService;

    @Autowired
    CustomerStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /*
     *   가게 전체 조회 //필터적용
     */
    @GetMapping("/{category}/{searchConditions}")
    public ResponseEntity<Map<String,Object>> getStoresFilter(@PathVariable Category category,
                                                        @PathVariable SearchConditions searchConditions) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStores(category,searchConditions));
    }

    /*
     *   가게 단건 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getCostumersStore(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(storeId));
    }
}
