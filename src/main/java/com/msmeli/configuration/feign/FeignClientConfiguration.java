package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    @Value("${meli.bearer.token}")
    private String bearerToken;

    @Bean
    public RequestInterceptor bearerTokenInterceptor(){
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
