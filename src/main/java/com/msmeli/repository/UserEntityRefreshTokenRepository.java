package com.msmeli.repository;

import com.msmeli.model.UserEntityRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRefreshTokenRepository extends JpaRepository<UserEntityRefreshToken, Long> {
    Optional<UserEntityRefreshToken> findByToken(String token);

    @Query("SELECT rt FROM UserEntityRefreshToken rt where rt.userEntity.username = ?1")
    Optional<UserEntityRefreshToken> findByUsername(String username);
}
