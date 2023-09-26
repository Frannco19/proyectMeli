package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-3292649208279826-092609-5ef7a8326c7208cb3bca5fa420ddf6d0-146409424";

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
