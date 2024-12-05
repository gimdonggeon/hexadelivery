package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        if (session.getAttribute(Const.LOGIN_USER) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "11로그인이 필요합니다.");
        }

        return true;
    }
}
