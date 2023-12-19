package com.msmeli.controller;

import com.msmeli.dto.response.ExceptionDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ExceptionDTO> handleSecurityException(SecurityException ex) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                                                    .mensaje( ex.getMessage())
                                                    .codigohttp(HttpStatus.UNAUTHORIZED.value())
                                                    .codigoError("Aca nuestro codigo interno")
                                                    .zone(ex.getZone())
                                                    .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionDTO> handleAppExeption(AppException ex){
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .mensaje( ex.getMessage())
                .codigohttp(ex.getCodeHtpp())
                .codigoError("Aca nuestro codigo interno")
                .zone(ex.getZone())
                .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.valueOf(ex.getCodeHtpp()));
    }
}
