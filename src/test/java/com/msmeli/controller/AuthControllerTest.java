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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private SellerService sellerService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(authController, "userEntityService", userEntityService);
        ReflectionTestUtils.setField(authController, "sellerService", sellerService);
        ReflectionTestUtils.setField(authController, "authenticationManager", authenticationManager);
    }

    @Test
    public void testLogin() throws ResourceNotFoundException {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("username", "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userEntityService.userAuthenticateAndGetToken("username")).thenReturn(new UserAuthResponseDTO());

        ResponseEntity<UserAuthResponseDTO> responseEntity = authController.login(authRequestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userEntityService, times(1)).userAuthenticateAndGetToken("username");
    }

    @Test
    public void testLoginInvalidUser() throws ResourceNotFoundException {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("invalidUsername", "invalidPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(UsernameNotFoundException.class);

        try {
            authController.login(authRequestDTO);
        } catch (UsernameNotFoundException e) {
            // Expected exception
        }

        verify(userEntityService, never()).userAuthenticateAndGetToken(anyString());
    }

    @Test
    public void testRegisterSeller() throws ResourceNotFoundException, AlreadyExistsException {
        UserRegisterRequestDTO userEntity = new UserRegisterRequestDTO();
        when(sellerService.createSeller(any(UserRegisterRequestDTO.class))).thenReturn(new UserResponseDTO());

        ResponseEntity<UserResponseDTO> responseEntity = authController.registerSeller(userEntity);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(sellerService, times(1)).createSeller(userEntity);
    }

    @Test
    public void testRegisterEmployee() throws ResourceNotFoundException, AlreadyExistsException {
        EmployeeRegisterRequestDTO employeeRegisterDTO = new EmployeeRegisterRequestDTO();
        when(sellerService.createEmployee(any(EmployeeRegisterRequestDTO.class))).thenReturn(new UserResponseDTO());

        ResponseEntity<UserResponseDTO> responseEntity = authController.registerEmployee(employeeRegisterDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(sellerService, times(1)).createEmployee(employeeRegisterDTO);
    }

    @Test
    public void testRegisterSellerAlreadyExists() throws ResourceNotFoundException, AlreadyExistsException {
        UserRegisterRequestDTO userEntity = new UserRegisterRequestDTO();
        when(sellerService.createSeller(any(UserRegisterRequestDTO.class))).thenThrow(AlreadyExistsException.class);

        try {
            authController.registerSeller(userEntity);
        } catch (AlreadyExistsException e) {
            // Expected exception
        }

        verify(sellerService, times(1)).createSeller(userEntity);
    }

}

