package com.msmeli.service.services;

import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.UserEntity;

import java.util.List;

public interface IUserEntityService {
    UserResponseDTO create(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException;

    UserResponseDTO read(Long id) throws ResourceNotFoundException;

    List<UserResponseDTO> readAll() throws ResourceNotFoundException;

    UserEntity update(UserEntity userEntity) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
    UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException;
}
