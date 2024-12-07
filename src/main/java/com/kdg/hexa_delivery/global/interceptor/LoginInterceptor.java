package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.global.constant.Const;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.WrongAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new WrongAccessException(ExceptionType.NOT_LOGIN);
        }

        if (session.getAttribute(Const.LOGIN_USER) == null) {
            throw new WrongAccessException(ExceptionType.NOT_LOGIN);
        }

        return true;
    }
}
