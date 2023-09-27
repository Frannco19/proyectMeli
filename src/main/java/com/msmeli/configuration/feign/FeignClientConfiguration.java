package com.msmeli.configuration.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private String bearerToken = "APP_USR-8717495891911341-092619-c1252e765020619f2e0064f0c1f86489-20438228";

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return template -> template.header("Authorization", String.format("Bearer %s", bearerToken));
    }

}
