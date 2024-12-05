package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MerchantRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "세션이 끊어졌습니다.");
        }

        User loginUser = (User) session.getAttribute("LOGIN_USER");

        if (loginUser.getRole() != Role.MERCHANT) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "세션이 끊어졌습니다.");
        }

        return true;
    }
}
