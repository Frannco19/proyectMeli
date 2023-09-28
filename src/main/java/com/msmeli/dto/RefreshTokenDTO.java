package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenDTO {

    private String access_token;

    private String token_type;

    private Integer expires_in;

    private String scope;

    private Integer user_id;

    private String refresh_token;

}
