package com.msmeli.service.services;

public interface TokenService {

    public void saveToken();
    public void updateToken(String access_token);
    public String getRefreshToken(String username);

}
