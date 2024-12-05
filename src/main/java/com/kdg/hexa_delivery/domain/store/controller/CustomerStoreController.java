package com.kdg.hexa_delivery.domain.store.controller;

import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers/stores")
public class CustomerStoreController {

    StoreService storeService;

    @Autowired
    CustomerStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /*
     *   가게 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStores() {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStores());
    }

    /*
     *   가게 단건 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getCostumersStore(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(storeId));
    }
}
