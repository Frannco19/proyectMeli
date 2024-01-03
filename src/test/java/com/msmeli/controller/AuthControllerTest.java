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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLogin() throws ResourceNotFoundException, AppException {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("username", "password");
        UserAuthResponseDTO expectedResponse = new UserAuthResponseDTO();

        when(userEntityService.userAuthenticateAndGetToken("username", "password")).thenReturn(expectedResponse);

        ResponseEntity<UserAuthResponseDTO> responseEntity = authController.login(authRequestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(userEntityService, times(1)).userAuthenticateAndGetToken("username", "password");
    }

    @Test
    void testRegisterSeller() throws ResourceNotFoundException, AlreadyExistsException {
        UserRegisterRequestDTO userEntity = new UserRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();

        when(sellerService.createSeller(any(UserRegisterRequestDTO.class))).thenReturn(expectedResponse);

        ResponseEntity<UserResponseDTO> responseEntity = authController.registerSeller(userEntity);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(sellerService, times(1)).createSeller(any(UserRegisterRequestDTO.class));
    }

    @Test
    void testRegisterEmployee() throws ResourceNotFoundException, AlreadyExistsException {
        EmployeeRegisterRequestDTO employeeRegisterDTO = new EmployeeRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();

        when(sellerService.createEmployee(any(EmployeeRegisterRequestDTO.class))).thenReturn(expectedResponse);

        ResponseEntity<UserResponseDTO> responseEntity = authController.registerEmployee(employeeRegisterDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(sellerService, times(1)).createEmployee(any(EmployeeRegisterRequestDTO.class));
    }

}
