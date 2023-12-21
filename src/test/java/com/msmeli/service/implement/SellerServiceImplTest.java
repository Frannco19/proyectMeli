package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
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
    @Mock
    private SecurityContext securityContext;
    @Mock
    private TokenRequestDTO tokenRequestDTO;

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
        // ... Configuración de tus objetos necesarios

        // Crear instancia de SellerServiceImpl
        SellerServiceImpl sellerService = new SellerServiceImpl(
                sellerRepository, sellerRefactorRepository, meliFeignClient,
                userEntityService, employeeRepository, mapper
        );

        // Crear instancia de TokenRequestDTO
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();

        // Configurar campos usando reflexión
        setPrivateField(tokenRequestDTO, "code", "tuCodigo");
        setPrivateField(tokenRequestDTO, "clientSecret", "tuClientSecret");
        setPrivateField(tokenRequestDTO, "clientId", "tuClientId");
        setPrivateField(tokenRequestDTO, "redirectUri", "tuRedirectUri");
        setPrivateField(tokenRequestDTO, "codeVerifier", "123");
        setPrivateField(tokenRequestDTO, "grantType", "authorization_code");

        // Llamar al método saveToken con tu instancia modificada de TokenRequestDTO
        sellerService.saveToken("tuTG");

        // Realizar aserciones o verificaciones necesarias
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Manejar la excepción según tus necesidades
            e.printStackTrace();
        }
    }
    @Test
    public void testRefreshToken() {
        // Configurar el entorno de prueba
        Long userId = 1L;
        SellerRefactor seller = new SellerRefactor();
        seller.setRefreshToken("refresh_token");
        when(sellerRefactorRepository.findById(userId)).thenReturn(Optional.of(seller));

        TokenRequestDTO expectedTokenRequest = new TokenRequestDTO();
        expectedTokenRequest.setRefresh_token("refresh_token");
        expectedTokenRequest.setClient_id("your_client_id");
        expectedTokenRequest.setClient_secret("your_client_secret");
        expectedTokenRequest.setRedirect_uri("your_redirect_uri");
        expectedTokenRequest.setGrant_type("refresh_token");

        TokenResposeDTO mockedTokenResponse = new TokenResposeDTO();
        // Configurar el objeto TokenResponse según lo que esperas obtener
        when(meliFeignClient.refreshToken(expectedTokenRequest)).thenReturn(mockedTokenResponse);

        // Ejecutar el método que deseas probar
        TokenResposeDTO actualTokenResponse = sellerService.refreshToken();

        // Verificar que el método del repositorio se llamó con el ID correcto
        verify(sellerRefactorRepository).findById(userId);

        // Verificar que el método del cliente Feign se llamó con la solicitud de token esperada
        verify(meliFeignClient).refreshToken(expectedTokenRequest);

        // Verificar cualquier otra condición o resultado esperado
        // Puedes usar las aserciones de JUnit para esto
        assertNotNull(actualTokenResponse);
        // Agrega más aserciones según sea necesario
    }

    @Test
    public void testGetAllEmployees() {
        // Configurar comportamiento simulado para el mock de repository
        Employee employee1 = new Employee(/* Configurar según tus necesidades */);
        Employee employee2 = new Employee(/* Configurar según tus necesidades */);
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // Configurar comportamiento simulado para el mock de mapper
        EmployeesResponseDto responseDto1 = new EmployeesResponseDto(/* Configurar según tus necesidades */);
        EmployeesResponseDto responseDto2 = new EmployeesResponseDto(/* Configurar según tus necesidades */);
        List<EmployeesResponseDto> expectedResponseDtos = Arrays.asList(responseDto1, responseDto2);

        when(mapper.map(any(), eq(EmployeesResponseDto.class)))
                .thenReturn(responseDto1, responseDto2);

        // Llamar al método que estás probando
        List<EmployeesResponseDto> result = sellerService.getAllEmployees();

        // Verificar que los métodos necesarios se hayan llamado
        verify(employeeRepository, times(1)).findAll();
        verify(mapper, times(expectedEmployees.size())).map(any(), eq(EmployeesResponseDto.class));

        // Realizar aserciones
        assertEquals(expectedResponseDtos, result);
    }
}
