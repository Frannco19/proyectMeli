package com.msmeli.service.implement;

import com.msmeli.model.Token;
import com.msmeli.repository.TokenRepository;
import com.msmeli.service.services.TokenService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void saveToken(){
        Token token = new Token();

        token.setRefresh_token("TG-64e7a8e091c3b50001c18333-1460630484");

        //TODO CAMBIAR EN CASO DE EMERGENCIA
        token.setAccess_token("APP_USR-8900314243904844-092909-72e95ec007bdcfc14d41a5975b16df83-1460630484");

        token.setUsername("ADMIN");

        tokenRepository.save(token);
    }

    @Override
    public void updateToken(String access_token) {

        Token token = tokenRepository.findByUsername("ADMIN");

        token.setAccess_token(access_token);

        tokenRepository.save(token);

    }

    @Override
    public String getRefreshToken(String username) {
        Token token = tokenRepository.findByUsername(username);
        return token.getRefresh_token();
    }

    public String getAccessToken(String username){
        Token token = tokenRepository.findByUsername(username);
        return token.getAccess_token();
    }
}
