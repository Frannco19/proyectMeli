package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-1541593069766345-092122-d482dabeb21a6614f3dbdc09c09854f8-179995772";

    @Bean
    public RequestInterceptor bearerTokenInterceptor(){
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
