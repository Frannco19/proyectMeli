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
import org.springframework.beans.factory.annotation.Value;
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


    public SellerServiceImpl(SellerRepository sellerRepository, SellerRefactorRepository sellerRefactorRepository, MeliFeignClient meliFeignClient, UserEntityService userEntityService, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.meliFeignClient = meliFeignClient;
        this.userEntityService = userEntityService;
        this.mapper = mapper;
    }

    /**
     * Recupera la información de un vendedor mediante su ID.
     *
     * Este método busca y devuelve un Optional que puede contener la información del vendedor
     * correspondiente al ID proporcionado. Si no se encuentra un vendedor con el ID dado,
     * el Optional estará vacío.
     *
     * @param id Identificador único del vendedor.
     * @return Optional<Seller> Un Optional que puede contener la información del vendedor correspondiente al ID proporcionado.
     *                         Si no se encuentra un vendedor con el ID dado, el Optional estará vacío.
     */
    @Override
    public Optional<Seller> getSeller(Integer id) {
        return sellerRepository.findById(id);
    }

    /**
     * Crea un nuevo vendedor en el sistema utilizando la información proporcionada en la solicitud.
     *
     * Este método utiliza un objeto SellerRequestDTO para mapear los datos de la solicitud a un objeto Seller
     * y luego guarda el nuevo vendedor en el repositorio de vendedores.
     *
     * @param sellerRequestDTO La información de la solicitud que contiene los detalles del nuevo vendedor.
     * @return Seller El objeto que representa al vendedor recién creado y almacenado en el sistema.
     */
    @Override
    public Seller create(SellerRequestDTO sellerRequestDTO) {
        Seller seller = mapper.map(sellerRequestDTO, Seller.class);
        return sellerRepository.save(seller);
    }

    /**
     * Crea un nuevo vendedor en el sistema utilizando la información proporcionada en la solicitud.
     *
     * Este método delega la creación del nuevo vendedor al servicio de entidades de usuario
     * y devuelve un objeto UserResponseDTO que contiene la información del vendedor recién creado.
     *
     * @param userRegisterRequestDTO La información de la solicitud que contiene los detalles del nuevo vendedor.
     * @return UserResponseDTO Un objeto que representa la respuesta con la información del vendedor recién creado.
     * @throws ResourceNotFoundException Si ocurre un error al buscar recursos necesarios para la creación del vendedor,
     *                                    se lanza una excepción ResourceNotFoundException con un mensaje explicativo.
     * @throws AlreadyExistsException Si ya existe un vendedor con la misma información proporcionada en la solicitud,
     *                                se lanza una excepción AlreadyExistsException con un mensaje explicativo.
     */
    @Override
    public UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException {
        //Seller seller = sellerRepository.findById(userRegisterRequestDTO.getSeller_id()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
        SellerRefactor seller = new SellerRefactor();
        return userEntityService.createSeller(userRegisterRequestDTO);
    }

    /**
     * Crea un nuevo empleado en el sistema utilizando la información proporcionada en la solicitud.
     *
     * Este método delega la creación del nuevo empleado al servicio de entidades de usuario
     * y devuelve un objeto UserResponseDTO que contiene la información del empleado recién creado.
     *
     * @param employeeRegisterDTO La información de la solicitud que contiene los detalles del nuevo empleado.
     * @return UserResponseDTO Un objeto que representa la respuesta con la información del empleado recién creado.
     * @throws ResourceNotFoundException Si ocurre un error al buscar recursos necesarios para la creación del empleado,
     *                                    se lanza una excepción ResourceNotFoundException con un mensaje explicativo.
     * @throws AlreadyExistsException Si ya existe un empleado con la misma información proporcionada en la solicitud,
     *                                se lanza una excepción AlreadyExistsException con un mensaje explicativo.
     */
    @Override
    public UserResponseDTO createEmployee(EmployeeRegisterRequestDTO employeeRegisterDTO) throws ResourceNotFoundException, AlreadyExistsException {
        return userEntityService.createEmployee(employeeRegisterDTO);
    }

    /**
     * Recupera una lista de todos los vendedores almacenados en el sistema.
     *
     * Este método consulta el repositorio de vendedores para obtener y devolver una lista que contiene
     * todos los vendedores disponibles en el sistema.
     *
     * @return List<Seller> Una lista que contiene todos los vendedores almacenados en el sistema.
     */
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
     * Renueva el token de acceso para el vendedor autenticado.
     *
     * Este método recupera el ID del vendedor autenticado, busca la información del vendedor en la base de datos,
     * y utiliza el token de actualización del vendedor para solicitar un nuevo token de acceso al servicio externo.
     *
     * @return TokenResposeDTO Un objeto que representa el nuevo token de acceso generado.
     *
     * @throws NoSuchElementException Si no se encuentra el vendedor en la base de datos con el ID proporcionado,
     *                                se lanza una excepción NoSuchElementException con un mensaje indicando la ausencia
     *                                del vendedor en la base de datos.
     */
    public TokenResposeDTO refreshToken() {
        Long id = getAuthenticatedUserId();
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));

        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO().builder()
                .refresh_token(seller.getRefreshToken())
                .client_id(meliClientId)
                .client_secret(meliClientSecret)
                .redirect_uri(meliRedirectUri)
                .grant_type("refresh_token")
                .build();

        return meliFeignClient.refreshToken(tokenRequestDTO);
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

    /**
     * Recupera la información del vendedor mediante su ID.
     *
     * Este método busca y devuelve la información del vendedor correspondiente al ID proporcionado.
     *
     * @param id Identificador único del vendedor.
     * @return SellerRefactor La información del vendedor correspondiente al ID proporcionado.
     * @throws ResourceNotFoundException Si no se encuentra un vendedor con el ID proporcionado,
     *                                    se lanza una excepción ResourceNotFoundException con un mensaje
     *                                    indicando que no se encontró el recurso.
     */
    @Override
    public SellerRefactor findById(Long id) throws ResourceNotFoundException {
        return sellerRefactorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(NOT_FOUND));
    }

    /**
     * Recupera la información del vendedor mediante su ID.
     *
     * Este método busca y devuelve la información del vendedor correspondiente al ID proporcionado.
     *
     * @param id Identificador único del vendedor.
     * @return Seller La información del vendedor correspondiente al ID proporcionado.
     * @throws ResourceNotFoundException Si no se encuentra un vendedor con el ID proporcionado,
     *                                    se lanza una excepción ResourceNotFoundException con un mensaje
     *                                    indicando que no se encontró el recurso.
     */
    @Override
    public Seller findById(Integer id) throws ResourceNotFoundException {
        return sellerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(NOT_FOUND));
    }

    /**
     * Recupera la información del vendedor mediante su ID único de vendedor.
     *
     * Este método busca y devuelve la información del vendedor correspondiente al ID único de vendedor proporcionado.
     *
     * @param sellerId ID único de vendedor que identifica de manera exclusiva a un vendedor.
     * @return Seller La información del vendedor correspondiente al ID único de vendedor proporcionado.
     * @throws ResourceNotFoundException Si no se encuentra un vendedor con el ID único de vendedor proporcionado,
     *                                    se lanza una excepción ResourceNotFoundException con un mensaje
     *                                    indicando que no se encontró el recurso.
     */
    @Override
    public Seller findBySellerId(Long sellerId) throws ResourceNotFoundException {
        return sellerRepository.findBySellerId(sellerId).orElseThrow(()->new ResourceNotFoundException("No hay sellers con ese id."));
    }

    /**
     * Recupera el ID del usuario autenticado actualmente.
     *
     * Este método utiliza el contexto de seguridad de Spring para obtener la información de autenticación del usuario.
     * Si el usuario está autenticado y la información principal es una instancia de UserEntityUserDetails,
     * se devuelve el ID del usuario; de lo contrario, se devuelve null.
     *
     * @return Long El ID del usuario autenticado actualmente, o null si el usuario no está autenticado o
     *              la información principal no es una instancia de UserEntityUserDetails.
     */
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
