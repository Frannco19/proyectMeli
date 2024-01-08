package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.TokenRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.EmployeesResponseDto;
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
import com.msmeli.service.services.UserEntityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SellerServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerRefactorRepository sellerRefactorRepository;

    @Mock
    private MeliFeignClient meliFeignClient;

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba unitaria para el método getSeller en la clase SellerService.
     *
     * Esta prueba verifica el correcto funcionamiento del método getSeller en SellerService.
     * Se asegura de que, al proporcionar un ID de vendedor, se recupere el vendedor correspondiente
     * del repositorio y se devuelva correctamente.
     */
    @Test
    public void testGetSeller() {
        int sellerId = 1;
        Seller expectedSeller = new Seller();
        Mockito.when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(expectedSeller));

        Optional<Seller> result = sellerService.getSeller(sellerId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedSeller, result.get());
    }

    /**
     * Prueba unitaria para el método create en la clase SellerService.
     *
     * Esta prueba verifica el correcto funcionamiento del método create en SellerService.
     * Se asegura de que se cree un nuevo vendedor a partir de un DTO, que el mapeo se realice
     * correctamente usando Mockito, y que el vendedor creado sea guardado adecuadamente en el
     * repositorio.
     */
    @Test
    public void testCreate() {
        SellerRequestDTO sellerRequestDTO = new SellerRequestDTO();
        Seller expectedSeller = new Seller();
        Mockito.when(mapper.map(ArgumentMatchers.eq(sellerRequestDTO), ArgumentMatchers.eq(Seller.class))).thenReturn(expectedSeller);
        Mockito.when(sellerRepository.save(expectedSeller)).thenReturn(expectedSeller);

        Seller result = sellerService.create(sellerRequestDTO);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSeller, result);
        Mockito.verify(sellerRepository, Mockito.times(1)).save(expectedSeller);
    }

    @Test
    public void testCreateSeller() throws ResourceNotFoundException, AlreadyExistsException {
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        Mockito.when(userEntityService.createSeller(userRegisterRequestDTO)).thenReturn(expectedResponse);

        UserResponseDTO result = sellerService.createSeller(userRegisterRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResponse, result);
        Mockito.verify(userEntityService, Mockito.times(1)).createSeller(userRegisterRequestDTO);
    }

    /**
     * Prueba unitaria para el método createEmployee en la clase SellerService.
     *
     * Esta prueba verifica el correcto funcionamiento del método createEmployee en SellerService.
     * Se asegura de que, al proporcionar un DTO de registro de empleado, se invoque correctamente el
     * servicio userEntityService para crear un nuevo empleado y se devuelva la respuesta esperada.
     *
     * @throws ResourceNotFoundException Si no se encuentra un recurso necesario.
     * @throws AlreadyExistsException    Si el recurso ya existe y no se puede crear de nuevo.
     */
    @Test
    public void testCreateEmployee() throws ResourceNotFoundException, AlreadyExistsException {
        EmployeeRegisterRequestDTO employeeRegisterDTO = new EmployeeRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        Mockito.when(userEntityService.createEmployee(employeeRegisterDTO)).thenReturn(expectedResponse);

        UserResponseDTO result = sellerService.createEmployee(employeeRegisterDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResponse, result);
        Mockito.verify(userEntityService, Mockito.times(1)).createEmployee(employeeRegisterDTO);
    }

    /**
     * Prueba unitaria para el método findAll en la clase SellerService.
     *
     * Esta prueba verifica el correcto funcionamiento del método findAll en SellerService.
     * Se asegura de que, al llamar al repositorio para obtener todos los vendedores, se devuelva
     * una lista que contiene el vendedor esperado.
     */
    @Test
    public void testFindAll() {
        Seller expectedSeller = new Seller();
        Mockito.when(sellerRepository.findAll()).thenReturn(Collections.singletonList(expectedSeller));

        List<Seller> result = sellerService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedSeller, result.get(0));
    }

    @Test
    public void testSaveToken() {
        Long sellerId = 1L;
        String TG = "testToken";
        SellerRefactor seller = new SellerRefactor();
        Mockito.when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
        Mockito.when(meliFeignClient.tokenForTG(tokenRequestDTO)).thenReturn(new TokenResposeDTO());

        TokenResposeDTO result = sellerService.saveToken(TG);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testRefreshToken() {
        Long sellerId = 1L;
        SellerRefactor seller = new SellerRefactor();

        Mockito.when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        TokenResposeDTO result = sellerService.refreshToken();

        Assertions.assertNotNull(result);
    }


//    @Test
//    public void testUpdateToken() {
//        // Arrange
//        Long sellerId = 1L;
//        String TG = "testToken";
//        SellerRefactor seller = new SellerRefactor();
//        Mockito.when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));
//
//        // Configura un TokenResponseDTO simulado que debería devolver meliFeignClient
//        TokenResposeDTO simulatedTokenResponse = new TokenResposeDTO();
//        Mockito.when(meliFeignClient.tokenForTG(ArgumentMatchers.any(TokenRequestDTO.class))).thenReturn(simulatedTokenResponse);
//
//        // Act
//        TokenResposeDTO result = sellerService.updateToken(TG);
//
//        // Assert
//        Assertions.assertNotNull(result);
//        // Añade más aserciones según tu lógica
//    }


    @Test
    public void testGetEmployeesBySellerId() throws ResourceNotFoundException {
        Long sellerId = 1L;
        SellerRefactor seller = new SellerRefactor();
        Mockito.when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        List<Employee> employeeList = Collections.singletonList(new Employee());
        Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);

        List<EmployeesResponseDto> result = sellerService.getEmployeesBySellerId();

        Assertions.assertNotNull(result);
    }

    /**
     * Prueba unitaria para el método getAllEmployees en la clase SellerService.
     *
     * Esta prueba verifica el correcto funcionamiento del método getAllEmployees en SellerService.
     * Se asegura de que, al llamar al repositorio para obtener todos los empleados, se devuelva
     * una lista que contiene al menos un empleado.
     */
    @Test
    public void testGetAllEmployees() {
        List<Employee> allEmployees = Collections.singletonList(new Employee());
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);

        List<EmployeesResponseDto> result = sellerService.getAllEmployees();

        Assertions.assertNotNull(result);
    }
}

