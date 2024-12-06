package com.kdg.hexa_delivery.domain.store.service;

import com.kdg.hexa_delivery.domain.base.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.image.service.ImageService;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.dto.StoreRequestDto;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final ImageService imageService;

    @Autowired
    public StoreService(StoreRepository storeRepository, ImageService imageService) {
        this.storeRepository = storeRepository;
        this.imageService = imageService;
    }

    /*
     * 가게 등록 메서드
     */
    public StoreResponseDto createStore(User user, StoreRequestDto storeRequestDto, List<MultipartFile> storeImages) {

        // 가게 생성가능 확인
        if(isValidStoreCount(user.getId())){
            throw new RuntimeException("영업중인 가게가 3개 이상입니다. 더 이상 생성 할 수 없습니다.");
        }

        Store store = new Store(user, storeRequestDto.getStoreName(),
                storeRequestDto.getCategory(),storeRequestDto.getPhone(),
                storeRequestDto.getAddress(),storeRequestDto.getStoreDetail(),
                storeRequestDto.getOpeningHours(),storeRequestDto.getClosingHours(),
                storeRequestDto.getMinimumOrderValue(),
                Status.NORMAL);


        Store savedStore = storeRepository.save(store);


        // 이미지 s3 서버에 업로드 후 url 받아오기
        List<Image> imageUrls = imageService.takeImages(storeImages, savedStore.getStoreId(), ImageOwner.STORE);


        return StoreResponseDto.toDto(savedStore, imageUrls);
    }

    /*
     * 내 가게 전체조회 메서드
     */

    public List<StoreResponseDto> getMyStores(Long userId) {
        return storeRepository.findAllByUser_Id(userId).stream().map(
                store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
    }

    /*
     * 가게 전체조회 메서드
     */
    public List<StoreResponseDto> getStores() {
        return storeRepository.findAllByStatusNORMAL().stream().map(
                store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))
        ).toList();
    }

    /*
     * 가게 단건 조회 메서드
     */
    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        return StoreResponseDto.toDto(store, imageService.findImages(storeId, ImageOwner.STORE));
    }

    /*
     *  가게 수정 메서드
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, String storeName,
                                        String category, String phone,
                                        String address, String storeDetail,
                                        String openingHours, String closingHours,
                                        Integer minimumOrderValue, List<MultipartFile> storeImages) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(store.getStatus() != Status.NORMAL){
            throw new RuntimeException("가게가 폐업하였습니다.");
        }

        // 이미지 s3 서버에 업로드 후 url 받아오기
        List<Image> imageUrls = imageService.takeImages(storeImages, store.getStoreId(), ImageOwner.STORE);


        store.updateStore(storeName, category,phone,address,storeDetail,openingHours,closingHours,minimumOrderValue);

        storeRepository.save(store);

        return StoreResponseDto.toDto(store, imageUrls);
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
        int count = storeRepository.findAllByUser_UserIdAndStatusOpen(userId);

        return count >= 3;
    }
}
