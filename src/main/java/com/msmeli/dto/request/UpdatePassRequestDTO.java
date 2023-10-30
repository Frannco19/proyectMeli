package com.msmeli.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Clase de solicitud para actualizar la contraseña de usuario")
public class UpdatePassRequestDTO {
    @NotBlank(message = "Ingrese una contraseña actual.")
    private String currentPassword;
    @NotBlank(message = "Ingrese una nueva contraseña.")
    @Size(min = 3, message = "La nueva contraseña debe tener al menos 3 caracteres.")
    private String password;
    @NotBlank(message = "Reingrese la nueva contraseña.")
    private String rePassword;
}
