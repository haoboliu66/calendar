package com.haoboliu.config;

import com.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/account/login", "/account/validateUsername","/account/register");
//    }
//
//    // new JwtInterceptor() injection
//    @Bean
//    public JwtInterceptor authenticationInterceptor() {
//        return new JwtInterceptor();
//    }
}
