package com.rainlf.weixin.v3.infa.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rain
 * @date 7/20/2024 6:12 AM
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${auth.path.enabled}")
    private String[] authPathEnabled;
    @Value("${auth.path.excluded}")
    private String[] authPathExcluded;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Assert.notEmpty(authPathEnabled, "auth.path.enabled is empty");
        Assert.notEmpty(authPathExcluded, "auth.path.excluded is empty");
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(authPathEnabled) // 拦截所有请求
                .excludePathPatterns(authPathExcluded); // 排除这些路径
    }
}
