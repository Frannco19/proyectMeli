package com.msmeli.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
