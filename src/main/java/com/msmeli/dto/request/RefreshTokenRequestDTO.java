package com.msmeli.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenRequestDTO {

    private String grant_type;

    private String client_id;

    private String client_secret;

    private String refresh_token;


}
