package com.kdg.hexa_delivery.global.config;

import com.kdg.hexa_delivery.global.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String[] TOKEN_LOGIN_REQUIRED_PATH_PATTERNS = {"/api/users/kakao/settingRoles", "/api/users/kakao/logout"};
    private static final String[] LOGIN_REQUIRED_PATH_PATTERNS = {"/api/**"};
    private static final String[] LOGIN_EXCLUDE_PATH_PATTERNS = {"/api/users/signup", "/api/users/login/**", "/api/users/kakao/login", "/api/users/kakao/loginRedirect", "/api/users/kakao/settingRoles, /api/users/kakao/logout"};
    private static final String[] CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/customers/**"};
    private static final String[] OWNER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/owners/**"};
    private static final String[] ADMIN_ROLE_REQUIRED_PATH_PATTERNS = {"/api/admins/**"};

    private final LoginInterceptor loginInterceptor;
    private final CustomerRoleInterceptor customerRoleInterceptor;
    private final OwnerRoleInterceptor ownerRoleInterceptor;
    private final AdminRoleInterceptor adminRoleInterceptor;
    private final TokenLoginInterceptor tokenLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenLoginInterceptor)
                .addPathPatterns(TOKEN_LOGIN_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(LOGIN_REQUIRED_PATH_PATTERNS)
                .excludePathPatterns(LOGIN_EXCLUDE_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);

        registry.addInterceptor(customerRoleInterceptor)
                .addPathPatterns(CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 2);

        registry.addInterceptor(ownerRoleInterceptor)
                .addPathPatterns(OWNER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 3);

        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns(ADMIN_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 4);
    }
}
