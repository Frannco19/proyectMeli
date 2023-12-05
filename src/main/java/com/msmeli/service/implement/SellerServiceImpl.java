package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.TokenRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.SellerRepository;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;
    private final SellerRefactorRepository sellerRefactorRepository;
    private final MeliFeignClient meliFeignClient;
    private final UserEntityService userEntityService;
    private ModelMapper mapper;
    private static final String NOT_FOUND = "Seller no encontrado.";


    public SellerServiceImpl(SellerRepository sellerRepository, SellerRefactorRepository sellerRefactorRepository, MeliFeignClient meliFeignClient, UserEntityService userEntityService, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.meliFeignClient = meliFeignClient;
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
    public UserResponseDTO createEmployee(EmployeeRegisterRequestDTO employeeRegisterDTO) throws ResourceNotFoundException, AlreadyExistsException {
        return userEntityService.createEmployee(employeeRegisterDTO);
    }

    @Override
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    /**
     * Este metodo se enecarga de guardar por primera vez el token(mercadoLibre) dentro de la
     * entidad seller que haya solicitido el endpoint
     * @param TG
     * @return
     */
    @Override
    public TokenResposeDTO saveToken(String TG) {
        Long id = getAuthenticatedUserId();
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO().builder()
                .code(TG)
                .client_secret("FkXpw1hbXHzxUhflOBcE9EOVGkeYTXp7")
                .client_id("1798757980849345")
                .redirect_uri("https://ml.gylgroup.com/")
                .code_verifier("123")
                .grant_type("authorization_code").build();
        TokenResposeDTO tokenResposeDTO = meliFeignClient.tokenForTG(tokenRequestDTO);

        seller.setTokenMl(tokenResposeDTO.getAccess_token());
        seller.setRefreshToken(tokenResposeDTO.getRefresh_token());
        seller.setMeliID(tokenResposeDTO.getUser_id());
        sellerRefactorRepository.save(seller);
        return null;


    }

    @Override
    public SellerRefactor findById(Long id) throws ResourceNotFoundException {
        return sellerRefactorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(NOT_FOUND));
    }



    @Override
    public Seller findById(Integer id) throws ResourceNotFoundException {
        return sellerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(NOT_FOUND));
    }

    @Override
    public Seller findBySellerId(Long sellerId) throws ResourceNotFoundException {
        return sellerRepository.findBySellerId(sellerId).orElseThrow(()->new ResourceNotFoundException("No hay sellers con ese id."));
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntityUserDetails) {
            UserEntityUserDetails userDetails = (UserEntityUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        } else {
            return null;
        }
    }
}