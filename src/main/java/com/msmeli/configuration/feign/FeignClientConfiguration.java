package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-1541593069766345-092015-6eb57ad29b9d058990216f09799f7cda-179995772";

    @Bean
    public RequestInterceptor bearerTokenInterceptor(){
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
