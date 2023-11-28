package com.msmeli.service.services;

import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerService {

    Optional<Seller> getSeller(Integer id);

    Seller create(SellerRequestDTO sellerRequestDTO);

    UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException;

    UserResponseDTO createEmployee(EmployeeRegisterRequestDTO EmployeeRegister, String token) throws ResourceNotFoundException, AlreadyExistsException;
    Seller findById(Integer id) throws ResourceNotFoundException;

    Seller findBySellerId(Long sellerId) throws ResourceNotFoundException;

    List<Seller> findAll();
}
