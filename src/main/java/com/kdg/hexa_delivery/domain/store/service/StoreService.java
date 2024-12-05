package com.kdg.hexa_delivery.domain.store.service;

import com.kdg.hexa_delivery.domain.base.enums.State;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.dto.StoreRequestDto;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /*
     * 가게 등록 메서드
     */
    public StoreResponseDto createStore(User user, StoreRequestDto storeRequestDto) {

        // 가게 생성가능 확인
        if(isValidStoreCount(user.getId())){
            throw new RuntimeException("영업중인 가게가 3개 이상입니다. 더 이상 생성 할 수 없습니다.");
        }

        Store store = new Store(user, storeRequestDto.getStoreName(),
                storeRequestDto.getCategory(),storeRequestDto.getPhone(),
                storeRequestDto.getAddress(),storeRequestDto.getStoreDetail(),
                storeRequestDto.getOpeningHours(),storeRequestDto.getClosingHours(),
                storeRequestDto.getMinimumOrderValue(),
                State.OPEN);

        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.toDto(savedStore);
    }

    /*
     * 내 가게 전체조회 메서드
     */

    public List<StoreResponseDto> getMyStores(Long userId) {
        return storeRepository.findAllByUser_Id(userId).stream().map(StoreResponseDto::toDto).toList();
    }

    /*
     * 가게 전체조회 메서드
     */
    public List<StoreResponseDto> getStores() {
        return storeRepository.findAll().stream().map(StoreResponseDto::toDto).toList();
    }

    /*
     * 가게 단건 조회 메서드
     */
    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        return StoreResponseDto.toDto(store);
    }

    /*
     *  가게 수정 메서드
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, String storeName, String category, String phone, String address, String storeDetail, String openingHours, String closingHours, Integer minimumOrderValue ) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        store.updateStore(storeName, category,phone,address,storeDetail,openingHours,closingHours,minimumOrderValue);

        storeRepository.save(store);

        return StoreResponseDto.toDto(store);
    }

    /*
     *  가게 폐업 메서드
     */
    @Transactional
    public void deleteStore(Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        store.updateStoreStatus();

        storeRepository.save(store);
    }

    /*
     * 영업중인 가게가 3개 이상일경우
     */
    public boolean isValidStoreCount(Long userId) {
        int count = storeRepository.findAllByUser_UserIdAndClosureOpen(userId);

        return count >= 3;
    }
}
