package com.msmeli.service.services;

import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.SupplierResquestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.EmployeesResponseDto;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;

import java.util.List;
import java.util.Optional;

public interface SellerService {

    Optional<Seller> getSeller(Integer id);

    Seller create(SellerRequestDTO sellerRequestDTO);

    UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException, AppException;

    UserResponseDTO createEmployee(EmployeeRegisterRequestDTO EmployeeRegister) throws ResourceNotFoundException, AlreadyExistsException;
    Seller findById(Integer id) throws ResourceNotFoundException;

    Seller findBySellerId(Long sellerId) throws ResourceNotFoundException;

    List<Seller> findAll();

    TokenResposeDTO saveToken(String TG);

    SellerRefactor findById(Long id) throws ResourceNotFoundException;

    TokenResposeDTO refreshToken();

    List<EmployeesResponseDto> getEmployeesBySellerId() throws ResourceNotFoundException;

    List<EmployeesResponseDto> getAllEmployees();
    void addSupplier(SupplierResquestDTO supplierResquestDTO) throws ResourceNotFoundException, AppException;
}
