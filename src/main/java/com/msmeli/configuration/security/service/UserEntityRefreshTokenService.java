package com.msmeli.configuration.security.service;

import com.msmeli.model.UserEntity;
import com.msmeli.model.UserEntityRefreshToken;
import com.msmeli.repository.UserEntityRefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserEntityRefreshTokenService {
    private final UserEntityRefreshTokenRepository refreshTokenRepository;

    public UserEntityRefreshTokenService(UserEntityRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserEntityRefreshToken createRefreshToken(UserEntity userEntity) {
        UserEntityRefreshToken refreshToken = UserEntityRefreshToken.builder()
                .userEntity(userEntity)
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<UserEntityRefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<UserEntityRefreshToken> findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }


}
