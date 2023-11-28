package com.msmeli.service.implement;

import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.SellerRepository;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;
    private final UserEntityService userEntityService;
    private ModelMapper mapper;
    private static final String NOT_FOUND = "Seller no encontrado.";


    public SellerServiceImpl(SellerRepository sellerRepository, UserEntityService userEntityService, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.userEntityService = userEntityService;
        this.mapper = mapper;
    }

    @Override
    public Optional<Seller> getSeller(Integer id) {
        return sellerRepository.findById(id);
    }

    @Override
    public Seller create(SellerRequestDTO sellerRequestDTO) {
        Seller seller = mapper.map(sellerRequestDTO, Seller.class);
        return sellerRepository.save(seller);
    }

    @Override
    public UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException {
        //Seller seller = sellerRepository.findById(userRegisterRequestDTO.getSeller_id()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
        SellerRefactor seller = new SellerRefactor();
        return userEntityService.createSeller(userRegisterRequestDTO);
    }

    @Override
    public UserResponseDTO createEmployee(EmployeeRegisterRequestDTO employeeRegisterDTO, String token) throws ResourceNotFoundException, AlreadyExistsException {
        return userEntityService.createEmployee(employeeRegisterDTO,token);
    }

    @Override
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @Override
    public Seller findById(Integer id) throws ResourceNotFoundException {
        return sellerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(NOT_FOUND));
    }

    @Override
    public Seller findBySellerId(Long sellerId) throws ResourceNotFoundException {
        return sellerRepository.findBySellerId(sellerId).orElseThrow(()->new ResourceNotFoundException("No hay sellers con ese id."));
    }
}
