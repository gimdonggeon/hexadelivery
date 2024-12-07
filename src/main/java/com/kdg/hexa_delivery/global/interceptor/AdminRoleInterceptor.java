package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.domain.user.enums.Role;
import com.kdg.hexa_delivery.domain.user.entity.User;
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
public class AdminRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        HttpSession session = request.getSession(false);

        if(session == null) {
            throw new WrongAccessException(ExceptionType.CUT_OF_SESSION);
        }

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(loginUser.getRole() != Role.ADMIN) {
            throw new WrongAccessException(ExceptionType.NEED_AUTHORITY_ADMIN);
        }

        return true;
    }

}
