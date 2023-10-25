package com.msmeli.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Clase de solicitud para autenticación de usuario.")
public class AuthRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
