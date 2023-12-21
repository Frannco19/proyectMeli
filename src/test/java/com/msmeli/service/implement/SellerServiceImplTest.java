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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Test
    public void testGetSeller() {
        // Arrange
        int sellerId = 1;
        Seller expectedSeller = new Seller();
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(expectedSeller));

        // Act
        Optional<Seller> result = sellerService.getSeller(sellerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedSeller, result.get());
    }

    @Test
    public void testCreate() {
        // Arrange
        SellerRequestDTO sellerRequestDTO = new SellerRequestDTO();
        Seller expectedSeller = new Seller();
        when(mapper.map(eq(sellerRequestDTO), eq(Seller.class))).thenReturn(expectedSeller);
        when(sellerRepository.save(expectedSeller)).thenReturn(expectedSeller);

        // Act
        Seller result = sellerService.create(sellerRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSeller, result);
        verify(sellerRepository, times(1)).save(expectedSeller);
    }

    @Test
    public void testCreateSeller() throws ResourceNotFoundException, AlreadyExistsException {
        // Arrange
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        when(userEntityService.createSeller(userRegisterRequestDTO)).thenReturn(expectedResponse);

        // Act
        UserResponseDTO result = sellerService.createSeller(userRegisterRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(userEntityService, times(1)).createSeller(userRegisterRequestDTO);
    }

    @Test
    public void testCreateEmployee() throws ResourceNotFoundException, AlreadyExistsException {
        // Arrange
        EmployeeRegisterRequestDTO employeeRegisterDTO = new EmployeeRegisterRequestDTO();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        when(userEntityService.createEmployee(employeeRegisterDTO)).thenReturn(expectedResponse);

        // Act
        UserResponseDTO result = sellerService.createEmployee(employeeRegisterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(userEntityService, times(1)).createEmployee(employeeRegisterDTO);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Seller expectedSeller = new Seller();
        when(sellerRepository.findAll()).thenReturn(Collections.singletonList(expectedSeller));

        // Act
        List<Seller> result = sellerService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedSeller, result.get(0));
    }

    @Test
    public void testSaveToken() {
        // Arrange
        Long sellerId = 1L;
        String TG = "testToken";
        SellerRefactor seller = new SellerRefactor();
        when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
        when(meliFeignClient.tokenForTG(tokenRequestDTO)).thenReturn(new TokenResposeDTO());

        // Act
        TokenResposeDTO result = sellerService.saveToken(TG);

        // Assert
        assertNotNull(result);
        // Añade más aserciones según tu lógica
    }

    @Test
    public void testRefreshToken() {
        // Arrange
        Long sellerId = 1L;
        SellerRefactor seller = new SellerRefactor();

        // Configura el comportamiento de sellerRefactorRepository
        when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act
        TokenResposeDTO result = sellerService.refreshToken();

        // Assert
        assertNotNull(result);
        // Añade más aserciones según tu lógica
    }


    @Test
    public void testUpdateToken() {
        // Arrange
        Long sellerId = 1L;
        String TG = "testToken";
        SellerRefactor seller = new SellerRefactor();
        when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Configura un TokenResponseDTO simulado que debería devolver meliFeignClient
        TokenResposeDTO simulatedTokenResponse = new TokenResposeDTO();
        when(meliFeignClient.tokenForTG(any(TokenRequestDTO.class))).thenReturn(simulatedTokenResponse);

        // Act
        TokenResposeDTO result = sellerService.updateToken(TG);

        // Assert
        assertNotNull(result);
        // Añade más aserciones según tu lógica
    }


    @Test
    public void testGetEmployeesBySellerId() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        SellerRefactor seller = new SellerRefactor();
        when(sellerRefactorRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        List<Employee> employeeList = Collections.singletonList(new Employee());
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // Act
        List<EmployeesResponseDto> result = sellerService.getEmployeesBySellerId();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAllEmployees() {
        // Arrange
        List<Employee> allEmployees = Collections.singletonList(new Employee());
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // Act
        List<EmployeesResponseDto> result = sellerService.getAllEmployees();

        // Assert
        assertNotNull(result);
        // Añade más aserciones según tu lógica
    }


}
