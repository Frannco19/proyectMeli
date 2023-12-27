package com.msmeli.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TokenRequestDTO {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String code_verifier;
    private String refresh_token;
}
