package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private final String bearerToken = "APP_USR-2526446047633125-092110-79b936d58a45ee41dfad28c1ea881308-343993558";

    @Bean
    public RequestInterceptor bearerTokenInterceptor(){
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
