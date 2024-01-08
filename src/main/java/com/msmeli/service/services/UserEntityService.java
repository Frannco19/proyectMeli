package com.msmeli.service.services;

import com.msmeli.dto.request.*;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserEntityService {
    UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException, AppException;
    UserResponseDTO createEmployee(EmployeeRegisterRequestDTO employeeRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException;

    UserResponseDTO read(Long id) throws ResourceNotFoundException;

    List<UserResponseDTO> readAll() throws ResourceNotFoundException;

    UserEntity update(UserEntity userEntity) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;

    UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException;

    UserAuthResponseDTO findByUsername(String username) throws ResourceNotFoundException;

    Map<String, String> recoverPassword(String username) throws ResourceNotFoundException, AppException;

    Map<String, String> resetPassword(String username) throws ResourceNotFoundException, AppException;

    Map<String, String> updatePassword(UpdatePassRequestDTO updatePassRequestDTO, String username) throws ResourceNotFoundException;

    UserAuthResponseDTO userRefreshToken(UserRefreshTokenRequestDTO refreshTokenRequestDTO) throws ResourceNotFoundException;

    UserAuthResponseDTO userAuthenticateAndGetToken(String username, String pass) throws ResourceNotFoundException, AppException;
    Long getAuthenticatedUserId();
}
