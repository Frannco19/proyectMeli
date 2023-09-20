package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-1541593069766345-092009-07a272a9c375a9c3b5209badb1c114f3-179995772";

    @Bean
    public RequestInterceptor bearerTokenInterceptor(){
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
