package com.kdg.hexa_delivery.global.interceptor;

import com.kdg.hexa_delivery.global.constant.Const;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.WrongAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenLoginInterceptor implements HandlerInterceptor {

    private final CacheManager cacheManager;

    public TokenLoginInterceptor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws WrongAccessException {

        HttpSession session = request.getSession(false);

        try {
            if (session != null && session.getAttribute(Const.LOGIN_USER) != null) {
                throw new WrongAccessException(ExceptionType.NOT_SOCIAL_LOGIN_USER);
            }
            String accessToken = request.getHeader("Authorization").substring(7);

            Cache kakaoUserId = cacheManager.getCache("KakaoUserId");

            if (kakaoUserId.get(accessToken) == null){
                throw new WrongAccessException(ExceptionType.WRONG_TOKEN);
            } else {
                return true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new WrongAccessException(ExceptionType.WRONG_FORM_HEADER);
        } catch (NullPointerException e) {
            throw new WrongAccessException(ExceptionType.NOT_LOGIN);
        }
    }
}
