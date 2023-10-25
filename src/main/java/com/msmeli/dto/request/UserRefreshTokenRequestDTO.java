package com.msmeli.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Clase de solicitud para token de refresco de usuario.")
public class UserRefreshTokenRequestDTO {
    private String refreshToken;
}
