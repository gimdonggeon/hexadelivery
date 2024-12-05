package com.kdg.hexa_delivery.global.config;

import com.kdg.hexa_delivery.global.interceptor.CustomerRoleInterceptor;
import com.kdg.hexa_delivery.global.interceptor.LoginInterceptor;
import com.kdg.hexa_delivery.global.interceptor.OwnerRoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
    private static final String[] LOGIN_REQUIRED_PATH_PATTERNS = {"/**"};
    private static final String[] LOGIN_EXCLUDE_PATH_PATTERNS = {"/api/users", "api/users/login"};
    private static final String[] CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/orders", "/api/orders/{orderId}/review"};
    private static final String[] OWNER_ROLE_REQUIRED_PATH_PATTERNS = {"/api/stores/business", "/api/stores/{storeId}", "/api/stores", "/api/orders/{orderId}", "/api/stores/{storeId}/menus/*"};

    private final LoginInterceptor loginInterceptor;
    private final CustomerRoleInterceptor customerRoleInterceptor;
    private final OwnerRoleInterceptor ownerRoleInterceptor;

    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor, CustomerRoleInterceptor customerRoleInterceptor, OwnerRoleInterceptor ownerRoleInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.customerRoleInterceptor = customerRoleInterceptor;
        this.ownerRoleInterceptor = ownerRoleInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(LOGIN_REQUIRED_PATH_PATTERNS)
                .excludePathPatterns(LOGIN_EXCLUDE_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(customerRoleInterceptor)
                .addPathPatterns(CUSTOMER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE+1);

        registry.addInterceptor(ownerRoleInterceptor)
                .addPathPatterns(OWNER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE+2);
    }
}
