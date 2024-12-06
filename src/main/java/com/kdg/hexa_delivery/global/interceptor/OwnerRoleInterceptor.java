package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class OwnerRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "세션이 끊어졌습니다.");
        }

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);

        if (loginUser.getRole() != Role.OWNER) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OWNER의 권한이 없습니다.");
        }

        return true;
    }
}
