package com.msmeli.controller;

import com.msmeli.dto.request.AuthRequestDTO;
import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserEntityService userEntityService;
    private final SellerService sellerService;


    public AuthController(UserEntityService userEntityService, SellerService sellerService, AuthenticationManager authenticationManager){
        this.userEntityService = userEntityService;
        this.sellerService = sellerService;

    }
    @PostMapping("/login")
    @Operation(summary = "Endpoint para autenticar usuario.")
    public ResponseEntity<UserAuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) throws ResourceNotFoundException, AppException {
            return ResponseEntity.status(HttpStatus.OK).body(userEntityService.userAuthenticateAndGetToken(authRequestDTO.getUsername(),authRequestDTO.getPassword()));

    }



    @PostMapping("/register-seller")
    @Operation(summary = "Endpoint para crear Seller, se espera UserRegisterRequestDTO.")
    public ResponseEntity<UserResponseDTO> registerSeller(@Valid @RequestBody UserRegisterRequestDTO userEntity) throws ResourceNotFoundException, AlreadyExistsException, AppException {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.createSeller(userEntity));
    }

    @PostMapping("/register-employee")
    @Operation(summary = "Endpoint para crear un Empleado se espera un UserRegisterRequestDTO")
    public ResponseEntity<UserResponseDTO>registerEmployee(@Valid @RequestBody EmployeeRegisterRequestDTO employeeRegisterDTO) throws ResourceNotFoundException, AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.createEmployee(employeeRegisterDTO));
    }


}
