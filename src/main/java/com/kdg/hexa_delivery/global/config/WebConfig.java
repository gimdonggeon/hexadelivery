package com.kdg.hexa_delivery.global.config;

import com.kdg.hexa_delivery.global.interceptor.CustomerRoleInterceptor;
import com.kdg.hexa_delivery.global.interceptor.LoginInterceptor;
import com.kdg.hexa_delivery.global.interceptor.OwnerRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String[] LOGIN_REQUIRED_PATH_PATTERNS = {"/api/**"};
    private static final String[] LOGIN_EXCLUDE_PATH_PATTERNS = {"/api/users/signup", "/api/users/login"};
    private static final String[] CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/customers/**"};
    private static final String[] OWNER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/owners/**"};

    private final LoginInterceptor loginInterceptor;
    private final CustomerRoleInterceptor customerRoleInterceptor;
    private final OwnerRoleInterceptor ownerRoleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(LOGIN_REQUIRED_PATH_PATTERNS)
                .excludePathPatterns(LOGIN_EXCLUDE_PATH_PATTERNS)
                .order(1);

        registry.addInterceptor(customerRoleInterceptor)
                .addPathPatterns(CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(2);

        registry.addInterceptor(ownerRoleInterceptor)
                .addPathPatterns(OWNER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(3);
    }

    //    @Bean
//    public FilterRegistrationBean loginFilter() {
//
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new LoginFilter());
//        filterRegistrationBean.setOrder(1);
//        filterRegistrationBean.addUrlPatterns("/*");
//
//        return filterRegistrationBean;
//    }

}
