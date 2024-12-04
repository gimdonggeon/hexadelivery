package com.kdg.hexa_delivery.domain.store.controller;
import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.store.dto.StoreRequestDto;
import com.kdg.hexa_delivery.domain.store.dto.StoreResponseDto;
import com.kdg.hexa_delivery.domain.store.service.StoreService;
import com.kdg.hexa_delivery.domain.store.dto.UpdateStoreRequestDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController("api/stores")
public class StoreController {

    StoreService storeService;

    @Autowired
    StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /*
     * 가게 등록 API
     */
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody @Valid StoreRequestDto storeRequestDto,
                                                        HttpServletRequest httpServletRequest) {

        // 가게 접근권한 확인 메서드
        User loginUser = validStoreAccess(httpServletRequest);
        // 가게 생성 가능 확인 메서드
        validStoreCreate(loginUser.getUserId());

        StoreResponseDto storeResponseDto = storeService.createStore(loginUser,storeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponseDto);
    }

    /*
     *   내 가게 전체 조회
     */
    @GetMapping("/me")
    public ResponseEntity<List<StoreResponseDto>> getMyStores(HttpServletRequest httpServletRequest) {
        // 가게 접근권한 확인 및 로그인세션 받기
        User loginUser = validStoreAccess(httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(storeService.getMyStores(loginUser.getUserId()));
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
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(storeId));
    }

    /*
     *   가게 수정
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId,
                                                        @RequestBody @Valid UpdateStoreRequestDto updateStoreRequestDto,
                                                        HttpServletRequest httpServletRequest) {
        // 가게 접근권한 확인 메서드
        User loginUser = validStoreAccess(httpServletRequest);
        //본인가게 확인 메서드
        validStoreAccessV2(storeId, loginUser);

        StoreResponseDto storeResponseDto = storeService.updateStore(
                storeId,
                updateStoreRequestDto.getStoreName(),
                updateStoreRequestDto.getCategory(),
                updateStoreRequestDto.getPhone(),
                updateStoreRequestDto.getAddress(),
                updateStoreRequestDto.getStoreDetail()
        );
        return ResponseEntity.status(HttpStatus.OK).body(storeResponseDto);
    }


    /*
     *   가게 폐업
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId,
                                              HttpServletRequest httpServletRequest) {
        // 가게 접근권한 확인 메서드
        User loginUser = validStoreAccess(httpServletRequest);
        //본인가게 확인 메서드
        validStoreAccessV2(storeId, loginUser);

        storeService.deleteStore(storeId);

        return ResponseEntity.status(HttpStatus.OK).body("가게가 폐업처리 되었습니다.");
    }





    // 가게 접근권한 확인 메서드
    private User validStoreAccess(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User LoginUser = (User) session.getAttribute("loginUser");

        // 사장님이 아니면 에러 발생
        if(LoginUser.getRole() != Role.MERCHANT){
            throw new RuntimeException("사장님이 아닙니다.");
        }
        return LoginUser;
    }

    // 본인가게 접근권한 확인 메서드
    private void validStoreAccessV2(Long storeId,User loginUser){
        // 사징님의 본인 가게가 아니면
        if(storeId.equals(loginUser.getStore().getStoreId())){
            throw new RuntimeException("본인의 가게가 아닙니다.");
        }
    }


    // 폐업중이 아닌 가게가 3개 이상일 경우 가게등록 검증
    private void validStoreCreate(Long userId) {
        if(storeService.isValidStoreCount(userId)){
            throw new RuntimeException("영업중인 가게가 3개 이상입니다. 더 이상 생성 할 수 없습니다.");
        }
    }

}
