package com.kdg.hexa_delivery.global.interceptor;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Date;


@Component
public class SocialLoginInterceptor implements HandlerInterceptor {

//    public boolean preHandle() {
//            String token = getAccessToken();
//            if (token != null) {
//                OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, new Date(), new Date(System.currentTimeMillis() + 3600000));  // 만료시간 예시
//
//                Date expiration = accessToken.getExpiresAt();
//                return expiration.before(new Date());  // 만료 여부 확인
//            }
//            return false;
//
//    }
}
