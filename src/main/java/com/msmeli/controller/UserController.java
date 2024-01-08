package com.msmeli.controller;

import com.msmeli.dto.request.AuthRequestDTO;
import com.msmeli.dto.request.UpdatePassRequestDTO;
import com.msmeli.dto.request.UserRefreshTokenRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://ml.gylgroup.com")
@RequestMapping("/meli/user")
public class UserController {

    private final UserEntityService userEntityService;
    private final SellerService sellerService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserEntityService userEntityService, SellerService sellerService, AuthenticationManager authenticationManager) {
        this.userEntityService = userEntityService;
        this.sellerService = sellerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/create")
    @Operation(summary = "Endpoint para crear usuario, se espera UserRegisterRequestDTO.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Usuario creado correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor.", content = @Content)})
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegisterRequestDTO userEntity) throws ResourceNotFoundException, AlreadyExistsException, AppException {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.createSeller(userEntity));
    }

    @GetMapping("/list")
    @Operation(summary = "Endpoint para listar usuarios.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Usuarios registrados en BD.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "No hay usuarios registrados.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<UserResponseDTO>> listarUsers() throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.readAll());
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Endpoint para autenticar usuario.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Devuelve el token y refreshToken del usuario.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<UserAuthResponseDTO> authenticateAndGetToken(@Valid @RequestBody AuthRequestDTO authRequestDTO) throws ResourceNotFoundException, AppException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body(userEntityService.userAuthenticateAndGetToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        }
        throw new UsernameNotFoundException("Solicitud de usuario invalida");
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Endpoint para refrescar token.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Devuelve el token y refreshToken del usuario.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<UserAuthResponseDTO> refreshToken(@RequestBody UserRefreshTokenRequestDTO refreshTokenRequestDTO) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.userRefreshToken(refreshTokenRequestDTO));
    }

    @GetMapping("/recover_password/{username}")
    @Operation(summary = "Endpoint para recuperar contraseña al recibir un mail.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Envia un mail con link al usuario para recuperar la contraseña.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<Map<String, String>> recoverPassword(@PathVariable String username) throws ResourceNotFoundException, AppException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.recoverPassword(username));
    }

    @GetMapping("/reset_password/{username}")
    @Operation(summary = "Endpoint para resetar la contraseña del usuario.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Se reseteo la contraseña correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable String username) throws ResourceNotFoundException, AppException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.resetPassword(username));
    }

    @PatchMapping("/add_role/{userId}")
    @Operation(summary = "Endpoint para agregar o quitar rol de ADMIN.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rol ADMIN agregado o eliminado correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    public ResponseEntity<UserResponseDTO> modifyUserRoles(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.modifyUserRoles(userId));
    }


    @PatchMapping("/modify_password")
    @Operation(summary = "Endpoint para recuperar actualizar la contraseña.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Se actualizó la contraseña correctamente.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}), @ApiResponse(responseCode = "400", description = "Solicitud erronea.", content = @Content), @ApiResponse(responseCode = "404", description = "Usuario no registrado.", content = @Content), @ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)})
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Map<String, String>> modifyPassword(@RequestBody UpdatePassRequestDTO updatePassRequestDTO, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.updatePassword(updatePassRequestDTO, authentication.getName()));
    }
}
