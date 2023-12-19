package com.msmeli.exception;


import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class SecurityException extends RuntimeException {


    private String zone;
    private int code;

     public SecurityException(String message, Throwable cause, String zone, int code) {
        super(message, cause);
        this.zone = zone;
        this.code = code;
    }

    public SecurityException(String message, String zone, int code) {
        super(message);
        this.zone = zone;
        this.code = code;
    }
}
