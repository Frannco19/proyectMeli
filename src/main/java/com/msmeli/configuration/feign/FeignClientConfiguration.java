package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-3292649208279826-092715-393f7d07acb0b1e2736e2dd9877b7bed-146409424";

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
