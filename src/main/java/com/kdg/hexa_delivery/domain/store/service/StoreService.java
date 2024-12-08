package com.kdg.hexa_delivery.domain.store.service;

import com.kdg.hexa_delivery.domain.advertise.enums.AdvertiseStatus;
import com.kdg.hexa_delivery.domain.advertise.enums.Category;
import com.kdg.hexa_delivery.domain.advertise.enums.SearchConditions;
import com.kdg.hexa_delivery.domain.advertise.repository.AdvertiseRepository;
import com.kdg.hexa_delivery.domain.image.entity.Image;
import com.kdg.hexa_delivery.domain.image.enums.ImageOwner;
import com.kdg.hexa_delivery.domain.image.service.ImageService;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.dto.StoreRequestDto;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.enums.Status;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.WrongAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final ImageService imageService;
    private final AdvertiseRepository advertiseRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, ImageService imageService,
                        AdvertiseRepository advertiseRepository) {
        this.storeRepository = storeRepository;
        this.advertiseRepository = advertiseRepository;
        this.imageService = imageService;

    }

    /*
     * 가게 등록 메서드
     */
    public StoreResponseDto createStore(User user, StoreRequestDto storeRequestDto, List<MultipartFile> storeImages) {

        // 가게 생성가능 확인
        if(isValidStoreCount(user.getId())){
            throw new WrongAccessException(ExceptionType.STORE_OVER_THREE);
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
     *  가게 전체조회 메서드
     */
    public Map<String,List<StoreResponseDto>> getStores(){
        Map<String,List<StoreResponseDto>> allStores = new LinkedHashMap<>();
        // 광고신청이 수락된 가게들
        List<StoreResponseDto> adStores = advertiseRepository.findStoreByAdvertiseStatus(AdvertiseStatus.ACCEPTED).stream().map(
                store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();;
        // Map 에 결과 삽입
        allStores.put("** 광고 **", adStores);

        // 모든 가게정보
        List<StoreResponseDto> stores = storeRepository.findAll().stream().map(
                store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
        // Map 에 결과 삽입
        allStores.put("** 모든가게 **", stores);

        return allStores;
    }



    /*
     * 가게 전체조회 메서드 :: 사용자 리뷰개수 , 별점 , 최근개업일자 , 광고
     */
    public Map<String,List<StoreResponseDto>> getStoresFilter(Category category, SearchConditions searchConditions) {
        Map<String,List<StoreResponseDto>> searchedStores = new LinkedHashMap<>();
        // 광고신청이 수락된 가게들 + 카테고리
        List<StoreResponseDto> adStores = advertiseRepository.findStoreByStoreCategoryAndStatus_Accepted(category, AdvertiseStatus.ACCEPTED).stream().map(
                store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
        // Map 에 결과 삽입
        searchedStores.put("** 광고 **", adStores);

        // 검색기준에따른 가게 정보
        List<StoreResponseDto> scStores = switch (searchConditions) {
            case RECENTLY -> storeRepository.findAllOrderByCreatedAt(category).stream().map(store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
            case SCOPE -> storeRepository.findAllOrderByRating(category).stream().map(store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
            case REVIEW -> storeRepository.findAllOrderByReviews(category).stream().map(store -> StoreResponseDto.toDto(store, imageService.findImages(store.getStoreId(), ImageOwner.STORE))).toList();
        };
        // Map 에 결과 삽입
        searchedStores.put("** "+ searchConditions.name()+" **", scStores);

        return searchedStores;
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
                                        Category category, String phone,
                                        String address, String storeDetail,
                                        String openingHours, String closingHours,
                                        Integer minimumOrderValue, List<MultipartFile> storeImages) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(store.getStatus() != Status.NORMAL){
            throw new WrongAccessException(ExceptionType.DELETED_STORE);
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
        int count = storeRepository.findAllByUser_UserIdAndStatusNORMAL(userId);

        return count >= 3;
    }
}
