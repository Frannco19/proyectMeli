package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.dto.request.*;
import com.msmeli.dto.response.EmployeesResponseDto;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Employee;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Supplier;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.SellerRepository;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.UserEntityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;
    private final SellerRefactorRepository sellerRefactorRepository;
    private final MeliFeignClient meliFeignClient;
    private final UserEntityService userEntityService;
    private final EmployeeRepository employeeRepository;
    private final SupplierService supplierService;

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


    public SellerServiceImpl(SellerRepository sellerRepository, SellerRefactorRepository sellerRefactorRepository, MeliFeignClient meliFeignClient, UserEntityService userEntityService, EmployeeRepository employeeRepository, SupplierService supplierService, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.meliFeignClient = meliFeignClient;
        this.userEntityService = userEntityService;
        this.employeeRepository = employeeRepository;
        this.supplierService = supplierService;
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
    public UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException, AppException {
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
     * <p>
     * Este método realiza las siguientes acciones:
     * 1. Obtiene el ID del vendedor autenticado.
     * 2. Busca y recupera la información del vendedor desde la base de datos mediante el ID.
     * 3. Construye una solicitud de actualización de token utilizando el refresh token del vendedor.
     * 4. Realiza la solicitud de actualización del token mediante el MeliFeignClient.
     * 5. Actualiza el nuevo refresh token y token de acceso en la entidad SellerRefactor.
     * 6. Guarda la entidad actualizada en la base de datos.
     * 7. Devuelve el objeto TokenResposeDTO que representa el nuevo token de acceso generado.
     *
     * @return TokenResposeDTO Un objeto que representa el nuevo token de acceso generado.
     *
     * @throws NoSuchElementException Si no se encuentra el vendedor en la base de datos con el ID proporcionado.
     * @throws IllegalStateException   Si la respuesta del MeliFeignClient es nula.
     *                                 Se lanza esta excepción cuando la respuesta es nula para evitar posibles
     *                                 NullPointerException al intentar acceder a sus propiedades.
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

        // Realiza la solicitud de actualización del token mediante el FeignClient
        TokenResposeDTO tokenResponse = meliFeignClient.refreshToken(tokenRequestDTO);

        // Verifica si la respuesta es nula antes de intentar acceder a sus propiedades
        if (tokenResponse != null) {
            // Actualizar el nuevo refresh token y tokenML en la entidad SellerRefactor
            seller.setRefreshToken(tokenResponse.getRefresh_token());
            seller.setTokenMl(tokenResponse.getAccess_token());

            // Guardar la entidad actualizada en la base de datos
            sellerRefactorRepository.save(seller);
        } else {
            // Manejar la situación en la que la respuesta del FeignClient es nula
            // Puedes lanzar una excepción, imprimir un mensaje de registro, etc.
            throw new IllegalStateException("La respuesta del refreshToken de MeliFeignClient es nula");
        }

        return tokenResponse;
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

    /**
     * Obtiene la lista de empleados asociados al vendedor autenticado.
     *
     * @return Lista de objetos EmployeesResponseDto que representan los empleados del vendedor.
     * @throws ResourceNotFoundException Si no se encuentra el recurso asociado al vendedor.
     */
    @Override
    public List<EmployeesResponseDto> getEmployeesBySellerId() throws ResourceNotFoundException {
        Long idSeller = getAuthenticatedUserId();
        SellerRefactor seller = findById(idSeller);
        List<Employee> employeeList = seller.getEmployees();

        List<EmployeesResponseDto> employeesListDTO = employeeList.stream()
                .map(employee -> {
                    EmployeesResponseDto dto = mapper.map(employee, EmployeesResponseDto.class);
                    dto.setId(employee.getId());
                    dto.setName(employee.getName());
                    dto.setLastname(employee.getLastname());
                    dto.setUsername(employee.getUsername());
                    dto.setEmail(employee.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());

        return employeesListDTO;
    }

    /**
     * Obtiene la lista de todos los empleados.
     *
     * @return Lista de objetos EmployeesResponseDto que representan todos los empleados.
     */
     @Override
    public List<EmployeesResponseDto> getAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();

        List<EmployeesResponseDto> employeesListDTO = allEmployees.stream()
                .map(employee -> mapper.map(employee, EmployeesResponseDto.class))
                .collect(Collectors.toList());

        return employeesListDTO;
    }

    /**
     * Este metodo se encarga en establecer la relacion en tre el proveedor y el seller
     * @param supplierResquestDTO datos del proveedor a relacionar con el seller
     * @throws ResourceNotFoundException
     * @throws AppException
     */
    @Override
    @Transactional
    public void addSupplier(SupplierResquestDTO supplierResquestDTO) throws ResourceNotFoundException, AppException {
         try{
             Long id = userEntityService.getAuthenticatedUserId();
             SellerRefactor seller = findById(id);
             Supplier supplier = supplierService.findById(supplierResquestDTO.getId());
             seller.addSupplier(supplier);
             sellerRefactorRepository.save(seller);
         }catch (Exception ex){
             throw new AppException("Error al agregar Suppliar a Seller","addSuplier",000,500);
         }
    }
}