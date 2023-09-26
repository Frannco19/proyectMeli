package com.msmeli.dto.request;

import jakarta.validation.constraints.Email;
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
public class UserRegisterRequestDTO {
    @NotBlank(message = "username should not be empty")
    @Size(min = 3, message = "username should have at least 3 characters")
    private String username;
    @NotBlank(message = "password should not be empty")
    @Size(min = 3, message = "password should have at least 3 characters")
    private String password;
    private String rePassword;
    @Email
    @NotBlank(message = "email should not be empty")
    private String email;
}
