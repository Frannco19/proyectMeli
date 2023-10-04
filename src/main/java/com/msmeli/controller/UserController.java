package com.msmeli.controller;

import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityRefreshTokenService;
import com.msmeli.dto.request.AuthRequestDTO;
import com.msmeli.dto.request.UpdatePassRequestDTO;
import com.msmeli.dto.request.UserRefreshTokenRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.UserEntityRefreshToken;
import com.msmeli.service.services.IUserEntityService;
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

@RestController
@CrossOrigin(origins = "http://201.216.243.146:10080")
@RequestMapping("/meli/user")
public class UserController {

    private final IUserEntityService userEntityService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserEntityRefreshTokenService refreshTokenService;

    public UserController(IUserEntityService userEntityService, JwtService jwtService, AuthenticationManager authenticationManager, UserEntityRefreshTokenService refreshTokenService) {
        this.userEntityService = userEntityService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegisterRequestDTO userEntity) throws ResourceNotFoundException, AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.create(userEntity));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<UserResponseDTO>> listarUsers() throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.readAll());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthResponseDTO> authenticateAndGetToken(@Valid @RequestBody AuthRequestDTO authRequestDTO) throws ResourceNotFoundException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserAuthResponseDTO userAuthResponseDTO = userEntityService.findByUsername(authRequestDTO.getUsername());
            userAuthResponseDTO.setToken(jwtService.generateToken(authRequestDTO.getUsername()));
            userAuthResponseDTO.setRefreshToken(refreshTokenService.findByUsername(userAuthResponseDTO.getUsername()).get().getToken());
            return ResponseEntity.status(HttpStatus.OK).body(userAuthResponseDTO);
        }
        throw new UsernameNotFoundException("Invalid user request");
    }

    @PostMapping("/refreshToken")
    public UserAuthResponseDTO refreshToken(@RequestBody UserRefreshTokenRequestDTO refreshTokenRequestDTO) throws ResourceNotFoundException {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken())
                .map(UserEntityRefreshToken::getUserEntity)
                .map(userEntity -> new UserAuthResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), jwtService.generateToken(userEntity.getUsername()), refreshTokenRequestDTO.getRefreshToken())).orElseThrow(() -> new ResourceNotFoundException("Refresh token is not in database"));
    }

    @GetMapping("/recover_password/{username}")
    public ResponseEntity<String> recoverPassword(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.recoverPassword(username));
    }

    @GetMapping("/reset_password/{username}")
    public ResponseEntity<String> resetPassword(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.resetPassword(username));
    }

    @PatchMapping("/add_role/{userId}")
    public ResponseEntity<UserResponseDTO> modifyUserRoles(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.modifyUserRoles(userId));
    }


    @PatchMapping("/modify_password")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> modifyPassword(@RequestBody UpdatePassRequestDTO updatePassRequestDTO, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.updatePassword(updatePassRequestDTO, authentication.getName()));
    }
}
