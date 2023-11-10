package com.msmeli.component;

import com.msmeli.dto.RefreshTokenDTO;
import com.msmeli.dto.request.RefreshTokenRequestDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.service.implement.TokenServiceImpl;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Getter
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final MeliFeignClient meliFeignClient;
    private final ModelMapper mapper;

    private final TokenServiceImpl tokenService;

    @Value("${meli.grantType}")
    private String grant_type;

    @Value("${meli.clientId}")
    private String client_id;

    @Value("${meli.clientSecret}")
    private String client_secret;

    private String token;

    public ScheduledTasks(MeliFeignClient meliFeignClient, ModelMapper mapper, TokenServiceImpl tokenService) {
        this.meliFeignClient = meliFeignClient;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    @Scheduled(fixedRate = 3600000)
    @Order(2)
    public void refreshToken(){
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO();

        refreshTokenRequestDTO.setGrant_type(grant_type);
        refreshTokenRequestDTO.setClient_id(client_id);
        refreshTokenRequestDTO.setClient_secret(client_secret);
        refreshTokenRequestDTO.setRefresh_token(tokenService.getRefreshToken("ADMIN"));

        RefreshTokenDTO refreshToken = meliFeignClient.refreshToken(refreshTokenRequestDTO);

        token = refreshToken.getAccess_token();

        tokenService.updateToken(token);

        log.info("TOKEN refreshToken: {}", refreshToken.getAccess_token());
        log.info("TOKEN : {}", token);

    }

}
