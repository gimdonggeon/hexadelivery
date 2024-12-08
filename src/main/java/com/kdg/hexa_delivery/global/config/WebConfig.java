package com.kdg.hexa_delivery.global.config;

import com.kdg.hexa_delivery.global.interceptor.AdminRoleInterceptor;
import com.kdg.hexa_delivery.global.interceptor.CustomerRoleInterceptor;
import com.kdg.hexa_delivery.global.interceptor.LoginInterceptor;
import com.kdg.hexa_delivery.global.interceptor.OwnerRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String[] LOGIN_REQUIRED_PATH_PATTERNS = {"/api/**"};
    private static final String[] LOGIN_EXCLUDE_PATH_PATTERNS = {"/api/users/signup", "/api/users/login/*", "/api/users/login/kakao/logout"};
    private static final String[] CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/customers/**"};
    private static final String[] OWNER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/owners/**"};
    private static final String[] ADMIN_ROLE_REQUIRED_PATH_PATTERNS = {"/api/admins/**"};

    private final LoginInterceptor loginInterceptor;
    private final CustomerRoleInterceptor customerRoleInterceptor;
    private final OwnerRoleInterceptor ownerRoleInterceptor;
    private final AdminRoleInterceptor adminRoleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(LOGIN_REQUIRED_PATH_PATTERNS)
                .excludePathPatterns(LOGIN_EXCLUDE_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(customerRoleInterceptor)
                .addPathPatterns(CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);

        registry.addInterceptor(ownerRoleInterceptor)
                .addPathPatterns(OWNER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 2);

        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns(ADMIN_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 3);
    }
}
