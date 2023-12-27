package com.msmeli.exception;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class AppException extends Exception{
    private String zone;
    private int codeHtpp;
    private int code;

    public AppException(String message, String zone, int code, int codeHtpp) {
        super(message);
        this.zone = zone;
        this.code = code;
        this.codeHtpp = codeHtpp;
    }
}
