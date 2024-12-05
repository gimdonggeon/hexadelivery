package com.kdg.hexa_delivery.domain.base.validation;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Validation {

    /**
     *  가게 접근권한 확인 메서드
     *
     * @param httpServletRequest  request 객체
     *
     * @return loginUser 로그인된 유저 정보
     *
     */
    public static User validStoreAccess(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        // 사장님이 아니면 에러 발생
        if(loginUser.getRole() != Role.OWNER){
            throw new RuntimeException("사장님이 아닙니다.");
        }
        return loginUser;
    }

    /**
     *  본인가게 접근권한 확인 메서드
     *
     * @param storeId 가게 id
     * @param loginUser 로그인 된 유저 정보
     *
     */
    public static void validMyStoreAccess(Long storeId,User loginUser){
        // 사장님의 본인 가게가 아니면 예외 발생
        if(loginUser.getStoreList().stream().filter(store -> store.getStoreId().equals(storeId)).findAny().isEmpty()){
            throw new RuntimeException("본인의 가게가 아닙니다.");
        }
    }
}
