package com.msmeli.configuration.security.service;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEntityUserDetailsService implements UserDetailsService {
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userEntityRepository.findByUsername(username);
        return userEntity.map(UserEntityUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
