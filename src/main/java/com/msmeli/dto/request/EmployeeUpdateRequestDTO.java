package com.msmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeUpdateRequestDTO {

    @Size(min = 3, message = "El nombre de usuario debe tener al menos 3 caracteres.")
    private String username;
    @Size(min = 3, message = "La contraseña debe tener al menos 3 caracteres.")
    private String password;
    private String rePassword;
    @Email
    private String email;
    private String nombre;
    private String apellido;
    private String rol;

}
