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
import com.msmeli.model.Employee;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.SellerRepository;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;
    private final SellerRefactorRepository sellerRefactorRepository;
    private final MeliFeignClient meliFeignClient;
    private final UserEntityService userEntityService;
    private final EmployeeRepository employeeRepository;

    private ModelMapper mapper;
    private static final String NOT_FOUND = "Seller no encontrado.";
    @Value("${meli.grantType}")
    private String meliGrantType;
    @Value("${meli.clientId}")
    private String meliClientId;
    @Value("${meli.clientSecret}")
    private String meliClientSecret;
    @Value("${meli.refresh.token}")
    private String meliRefreshToken;
    @Value("${meli.redirect.uri}")
    private String meliRedirectUri;


    public SellerServiceImpl(SellerRepository sellerRepository, SellerRefactorRepository sellerRefactorRepository, MeliFeignClient meliFeignClient, UserEntityService userEntityService, EmployeeRepository employeeRepository, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.meliFeignClient = meliFeignClient;
        this.userEntityService = userEntityService;
        this.employeeRepository = employeeRepository;
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
     * Guarda por primera vez el token de MercadoLibre dentro de la entidad Seller que ha solicitado el endpoint.
     * @param TG Token generado por MercadoLibre que se utilizará para obtener el token de acceso.
     * @return tokenResposeDTO que contiene la respuesta del proceso de guardado del token.
     * @throws NoSuchElementException Si no se encuentra al Seller en la base de datos.
     */
    @Override
    public TokenResposeDTO saveToken(String TG) {
        Long id = getAuthenticatedUserId();
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO().builder()
                .code(TG)
                .client_secret(meliClientSecret)
                .client_id(meliClientId)
                .redirect_uri(meliRedirectUri)
                .code_verifier("123")
                .grant_type("authorization_code").build();
        TokenResposeDTO tokenResposeDTO = meliFeignClient.tokenForTG(tokenRequestDTO);

        seller.setTokenMl(tokenResposeDTO.getAccess_token());
        seller.setRefreshToken(tokenResposeDTO.getRefresh_token());
        seller.setMeliID(tokenResposeDTO.getUser_id());
        sellerRefactorRepository.save(seller);
        return null;


    }

    /**
     * Actualiza el Token de MercadoLibre dentro de la entidad Seller.
     *
     * @param TG Token generado por MercadoLibre que se utilizará para obtener el nuevo token de acceso.
     * @return tokenResposeDTO que contiene la respuesta del proceso de actualización del token.
     * @throws NoSuchElementException Si no se encuentra al Seller en la base de datos.
     */
    @Override
    public TokenResposeDTO updateToken(String TG) {
        Long id = getAuthenticatedUserId();
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO().builder()
                .code(TG)
                .client_secret(meliClientSecret)
                .client_id(meliClientId)
                .redirect_uri(meliRedirectUri)
                .code_verifier("123")
                .grant_type(meliGrantType).build();
        TokenResposeDTO tokenResposeDTO = meliFeignClient.tokenForTG(tokenRequestDTO);

        // Actualizar el token y otros detalles si es necesario
        seller.setTokenMl(tokenResposeDTO.getAccess_token());
        seller.setRefreshToken(tokenResposeDTO.getRefresh_token());
        seller.setMeliID(tokenResposeDTO.getUser_id());
        sellerRefactorRepository.save(seller);

        return tokenResposeDTO;
    }

    /**
     * Actualiza el Access Token de MercadoLibre dentro de la entidad Seller.
     *
     * @param newAccessToken Nuevo Access Token a ser almacenado.
     * @throws NoSuchElementException Si no se encuentra al Seller en la base de datos.
     */
    @Override
    public void updateAccessToken(String newAccessToken) {
        Long id = getAuthenticatedUserId();
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));

        // Actualizar el access token si es necesario
        seller.setTokenMl(newAccessToken);
        sellerRefactorRepository.save(seller);
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

    @Override
    public List<Employee> getEmployeesBySellerId(Long sellerId) {
        SellerRefactor seller = null;
        try {
            seller = sellerRefactorRepository.findById(sellerId)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró el vendedor en la base de datos"));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return seller.getEmployees();
    }

    @Override
    public Map<String, Object> getEmployeesInfoBySellerId(Long sellerId) {
        List<Employee> employees = getEmployeesBySellerId(sellerId);
        int totalEmployees = employees.size();

        Map<String, Object> result = new HashMap<>();
        result.put("employees", employees);
        result.put("totalEmployees", totalEmployees);

        return result;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}