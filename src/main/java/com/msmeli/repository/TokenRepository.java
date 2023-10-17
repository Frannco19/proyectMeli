package com.msmeli.repository;

import com.msmeli.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    Token findByUsername(String username);

}
