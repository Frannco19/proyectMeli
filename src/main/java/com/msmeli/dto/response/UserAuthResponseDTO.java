package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.model.RoleEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String token;
    private String refreshToken;
    private List<RoleEntity> roles;
}
