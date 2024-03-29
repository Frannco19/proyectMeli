package com.msmeli.service.implement;

import com.msmeli.model.Token;
import com.msmeli.repository.TokenRepository;
import com.msmeli.service.services.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    @Value("${meli.refresh.token}")
    private String meliRefreshToken;

    @Value("${meli.access.token}")
    private String meliAccessToken;

    @Value("${meli.username.token}")
    private String meliUsernameToken;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void saveToken() {


        //TODO CAMBIAR LOGICA DE USAR EL SAVE TOKEN AGREGANDO USER

        if (tokenRepository.findAll().isEmpty() || !getAccessToken("ADMIN").matches(meliAccessToken)) {
            Token token = new Token();
            token.setAccess_token(meliAccessToken);
            token.setRefresh_token(meliRefreshToken);
            token.setUsername(meliUsernameToken);
            tokenRepository.save(token);
        }
        else {
            Token token = tokenRepository.findByUsername("ADMIN");
            token.setAccess_token(getAccessToken("ADMIN"));
            token.setRefresh_token(getRefreshToken("ADMIN"));
            tokenRepository.save(token);
        }
//        token.setAccess_token(meliAccessToken);
//        token.setAccess_token(getAccessToken("ADMIN"));




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

    public String getAccessToken(String username) {
        Token token = tokenRepository.findByUsername(username);
        return token.getAccess_token();
    }
}
