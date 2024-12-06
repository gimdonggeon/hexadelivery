package com.kdg.hexa_delivery.domain.advertise.service;

import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseDeclinedRequestDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseDeclinedResponseDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseRequestDto;
import com.kdg.hexa_delivery.domain.advertise.dto.AdvertiseResponseDto;
import com.kdg.hexa_delivery.domain.advertise.entity.Advertise;
import com.kdg.hexa_delivery.domain.advertise.repository.AdvertiseRepository;
import com.kdg.hexa_delivery.domain.base.enums.AdvertiseStatus;
import com.kdg.hexa_delivery.domain.store.entity.Store;
import com.kdg.hexa_delivery.domain.store.repository.StoreRepository;
import com.kdg.hexa_delivery.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertiseService {

    private final AdvertiseRepository advertiseRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public AdvertiseService(AdvertiseRepository advertiseRepository, StoreRepository storeRepository) {
        this.advertiseRepository = advertiseRepository;
        this.storeRepository = storeRepository;
    }

    // 가게 광고 등록
    public AdvertiseResponseDto createAdvertise(User user, Long storeId, AdvertiseRequestDto advertiseRequestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Advertise advertise = new Advertise(user, store,
                advertiseRequestDto.getBidPrice(), AdvertiseStatus.REQUESTED);

        // 광고 요청 저장
        Advertise savedAdvertise = advertiseRepository.save(advertise);

        return AdvertiseResponseDto.toDto(savedAdvertise);

    }

    // 광고 신청한 내 가게들 조회
    public List<AdvertiseResponseDto> getMyAdvertises(Long userId) {
        // 내 아이디로 광고신청한 가게들 전부 가져오기
        List<Advertise> advertises = advertiseRepository.findAllByUser_Id(userId);

        if(advertises.isEmpty()){
            throw new RuntimeException("해당 ID로 신청된 광고가 없습니다.");
        }

        return advertises.stream().map(AdvertiseResponseDto::toDto).toList();
    }

    // 광고 거절당한 내 가게 조회
    public AdvertiseDeclinedResponseDto getDeclinedMyAdvertise(Long storeId) {

        Advertise advertise = advertiseRepository.findByStore_StoreIdAndAdvertiseStatus_DECLINED(storeId,AdvertiseStatus.DECLINED);

        if(advertise==null){
            throw new RuntimeException("해당 ID로 신청된 광고중 거절당한 광고가 없습니다.");
        }

        return AdvertiseDeclinedResponseDto.toDto(advertise);
    }

    // 므든 광고신청 조회 :: 관리자
    public List<AdvertiseResponseDto> getAllAdvertises() {
        List<Advertise> advertises = advertiseRepository.findAll();

        return advertises.stream().map(AdvertiseResponseDto::toDto).toList();
    }

    // 광고신청 수락 :: 관리자
    public AdvertiseResponseDto advertiseAccept(Long advertiseId) {
        Advertise advertise = advertiseRepository.findByAdvertiseId(advertiseId);
        if(advertise==null){
            throw new RuntimeException("해당 가게로 신청된 광고가 없습니다.");
        }

        if(advertise.getAdvertiseStatus() != AdvertiseStatus.REQUESTED ){
            throw new RuntimeException("이미 처리된 요청입니다.");
        }
        // 광고 상태 수락으로 변경
        advertise.acceptAdvertiseStatus();
        // 저장
        Advertise savedAdvertise = advertiseRepository.save(advertise);
        return AdvertiseResponseDto.toDto(savedAdvertise);
    }
    // 광고신청 거부  :: 관리자 :: Advertise 상태 DECLINE 으로 변경
    public AdvertiseDeclinedResponseDto advertiseDecline(Long advertiseId, AdvertiseDeclinedRequestDto advertiseDeclinedRequestDto) {

        Advertise advertise = advertiseRepository.findByAdvertiseId(advertiseId);
        if(advertise==null){
            throw new RuntimeException("해당 가게로 신청된 광고가 없습니다.");
        }

        if(advertise.getAdvertiseStatus() != AdvertiseStatus.REQUESTED ){
            throw new RuntimeException("이미 처리된 요청입니다.");
        }

        advertise.declineAdvertiseStatus(advertiseDeclinedRequestDto.getDeclinedReason());
        Advertise savedAdvertise = advertiseRepository.save(advertise);
        return AdvertiseDeclinedResponseDto.toDto(savedAdvertise);

    }

}
