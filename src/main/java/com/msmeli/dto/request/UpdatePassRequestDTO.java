package com.msmeli.dto.request;

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
public class UpdatePassRequestDTO {
    @NotBlank(message = "password should not be empty")
    @Size(min=3, message = "password should have at least 3 characters")
    private String password;
    @NotBlank
    private String rePassword;
}
