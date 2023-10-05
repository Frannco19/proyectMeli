package com.msmeli.configuration.feign;

import com.msmeli.service.implement.TokenServiceImpl;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    private final TokenServiceImpl tokenService;

    public FeignClientConfiguration(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }


    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return template -> template.header("Authorization", String.format("Bearer %s", tokenService.getAccessToken("ADMIN")));
    }

}
