package com.example.actionprice.security.config;

import com.example.actionprice.security.interceptor.ExceptionHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final ExceptionHandlerInterceptor exceptionHandlerInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(exceptionHandlerInterceptor)
        .addPathPatterns("/**");
  }
}
