package com.zhikaixu.apipassenger.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // 遇到的问题：在未添加下面代码时，stringRedisTemplate (JwtInterceptor类)出现空指针错误
    // 原因：addInterceptors中直接new JwtInterceptor(), 这里的拦截器没有@Bean注解，所以不会给Spring管理，spring不会往这个对象注入 stringRedisTemplate，在拦截器加载时，内部需要的Bean还不存在，因此出现空指针
    // 解决方法：在配置拦截器时提供一个构造方法，并使用@Bean注解创建拦截器，使得拦截器被spring管理，并且在addInterceptors方法中使用构造方法
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 拦截的路径
                .addPathPatterns("/**")
                // 不拦截的路径
                .excludePathPatterns("/noauthTest")
                .excludePathPatterns("/verification-code")
                .excludePathPatterns("/verification-code-check")
                .excludePathPatterns("/token-refresh")
                .excludePathPatterns("/test-real-time-order/{orderId}")
                .excludePathPatterns("/error");
    }

}
