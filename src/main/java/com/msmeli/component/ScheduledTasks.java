package com.msmeli.component;

import com.msmeli.dto.RefreshTokenDTO;
import com.msmeli.dto.request.RefreshTokenRequestDTO;
import com.msmeli.feignClient.MeliFeignClient;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    private RefreshTokenRequestDTO refreshTokenRequestDTO;
    private final MeliFeignClient meliFeignClient;
    private final ModelMapper mapper;
    
    @Value("${meli.grantType}")
    private String grant_type;

    @Value("${meli.clientId}")
    private String client_id;

    @Value("${meli.clientSecret}")
    private String client_secret;

    @Value("${meli.refreshToken}")
    private String refresh_token;

    public ScheduledTasks(MeliFeignClient meliFeignClient, ModelMapper mapper) {
        this.meliFeignClient = meliFeignClient;
        this.mapper = mapper;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(fixedRate = 20000)
    public void refreshToken(){
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO();

        refreshTokenRequestDTO.setGrant_type(grant_type);
        refreshTokenRequestDTO.setClient_id(client_id);
        refreshTokenRequestDTO.setClient_secret(client_secret);
        refreshTokenRequestDTO.setRefresh_token(refresh_token);

        meliFeignClient.refreshToken(refreshTokenRequestDTO);

        RefreshTokenDTO refreshToken = mapper.map(refreshTokenRequestDTO, RefreshTokenDTO.class);

        log.info("TOKEN: {}", refreshToken.getAccess_token());

    }
}
