package com.msmeli.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionDTO {
    private String mensaje;
    private int codigohttp;
    private String zone;
    private String codigoError;
}
