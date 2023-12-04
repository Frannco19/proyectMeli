package com.msmeli.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TokenResposeDTO {
    private String access_token;
    private int expire_in;
    private String scope;
    private int user_id;
    private String refresh_token;
}
