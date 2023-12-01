package com.msmeli.controller;

import com.msmeli.dto.request.AuthRequestDTO;
import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserEntityService userEntityService;
    private final SellerService sellerService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserEntityService userEntityService, SellerService sellerService, AuthenticationManager authenticationManager){
        this.userEntityService = userEntityService;
        this.sellerService = sellerService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    @Operation(summary = "Endpoint para autenticar usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devuelve el token y refreshToken del usuario.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<UserAuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) throws ResourceNotFoundException{
        System.out.println("Estoy en el endpoid de logueo");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body(userEntityService.userAuthenticateAndGetToken(authRequestDTO.getUsername()));
        }
        throw new UsernameNotFoundException("Solicitud de usuario invalida");
    }



    @PostMapping("/register-seller")
    @Operation(summary = "Endpoint para crear Seller, se espera UserRegisterRequestDTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error del servidor.", content = @Content)})
    public ResponseEntity<UserResponseDTO> registerSeller(@Valid @RequestBody UserRegisterRequestDTO userEntity) throws ResourceNotFoundException, AlreadyExistsException{
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.createSeller(userEntity));
    }

    @PostMapping("/register-employee")
    @Operation(summary = "Endpoint para crear un Empleado se espera un UserRegisterRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error del servidor.", content = @Content)})
    public ResponseEntity<UserResponseDTO>registerEmployee(@Valid @RequestBody EmployeeRegisterRequestDTO employeeRegisterDTO) throws ResourceNotFoundException, AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.createEmployee(employeeRegisterDTO));
    }


}
