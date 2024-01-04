package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityRefreshTokenService;
import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Employee;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.EmailService;
import com.msmeli.service.services.RoleEntityService;
import com.msmeli.util.Role;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.msmeli.util.Role.SELLER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @Mock
    private RoleEntityService roleEntityService;

    @Mock
    private EmailService emailService;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService jwtService;

    @Mock
    private SellerRefactorRepository sellerRefactorRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserEntityServiceImpl userEntityServiceImpl;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateSeller() throws ResourceNotFoundException, AlreadyExistsException {
        // Arrange
        UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();
        requestDTO.setUsername("testUser");
        requestDTO.setPassword("testPassword");
        requestDTO.setRePassword("testPassword");

        SellerRefactor newSeller = new SellerRefactor();
        newSeller.setUsername(requestDTO.getUsername());

        when(userEntityRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());
        when(mapper.map(requestDTO, SellerRefactor.class)).thenReturn(newSeller);
        when(roleEntityService.findByName(SELLER)).thenReturn(new RoleEntity());
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(sellerRefactorRepository.save(any(SellerRefactor.class))).thenReturn(newSeller);

        UserResponseDTO responseDTO = userEntityServiceImpl.createSeller(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getUsername(), responseDTO.getUsername());
    }

    @Test
    void testReadUser() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Mockito.when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act
        try {
            UserResponseDTO responseDTO = userEntityServiceImpl.read(1L);

            // Assert
            Assertions.assertNotNull(responseDTO, "ResponseDTO should not be null");
            Assertions.assertEquals(1L, responseDTO.getId(), "Incorrect user ID");
        } catch (ResourceNotFoundException e) {
            Assertions.fail("Unexpected ResourceNotFoundException: " + e.getMessage());
        }
    }

    @SneakyThrows
    @Test
    public void testReadAllUsers() {
        // Definir el comportamiento esperado del repositorio
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        Mockito.when(userEntityRepository.findAll()).thenReturn(users);

        // Llamar al método y verificar el resultado
        try {
            List<UserResponseDTO> responseDTOs = userEntityServiceImpl.readAll();

            Assertions.assertNotNull(responseDTOs);
            Assertions.assertFalse(responseDTOs.isEmpty());
        } catch (ResourceNotFoundException e) {
            Assertions.fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @SneakyThrows
    @Test
    public void testUpdateUser() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Mockito.when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        Mockito.when(userEntityRepository.save(userEntity)).thenReturn(userEntity);

        // Llamar al método y verificar el resultado
        try {
            UserEntity updatedUserEntity = userEntityServiceImpl.update(userEntity);

            Assertions.assertNotNull(updatedUserEntity);
            Assertions.assertEquals(1L, updatedUserEntity.getId());
        } catch (ResourceNotFoundException e) {
            Assertions.fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @Test
    public void testDeleteUser() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Mockito.when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Llamar al método y verificar que no se lance ninguna excepción
        Assertions.assertDoesNotThrow(() -> userEntityServiceImpl.delete(1L));
    }

    @SneakyThrows
    @Test
    public void testModifyUserRoles() {
        // Definir el comportamiento esperado del repositorio y otros mocks
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Mockito.when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        RoleEntity adminRole = new RoleEntity();
        Mockito.when(roleEntityService.findByName(Role.ADMIN)).thenReturn(adminRole);

        // Llamar al método y verificar el resultado
        try {
            UserResponseDTO responseDTO = userEntityServiceImpl.modifyUserRoles(1L);

            //assertNotNull(responseDTO);
            Assertions.assertEquals(1L, responseDTO.getId());
        } catch (ResourceNotFoundException e) {
            Assertions.fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @SneakyThrows
    @Test
    public void testFindByUsername() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        Mockito.when(userEntityRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        // Llamar al método y verificar el resultado
        try {
            UserAuthResponseDTO responseDTO = userEntityServiceImpl.findByUsername("testuser");

            //assertNotNull(responseDTO);
            //assertEquals("testuser", responseDTO.getUsername());
        } catch (ResourceNotFoundException e) {
            Assertions.fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }


    @Test
    void testRecoverPassword() throws ResourceNotFoundException {
        // Configuración del mock
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");

        Mockito.when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Llamada al método que deseas probar
        Map<String, String> result = userEntityServiceImpl.recoverPassword(username);

        // Verificaciones
        Mockito.verify(emailService,  Mockito.times(1)).sendMail(ArgumentMatchers.eq("test@example.com"), ArgumentMatchers.eq("Recuperar contraseña"), ArgumentMatchers.anyString());
        Assertions.assertEquals("Correo electrónico de recuperación de contraseña enviado correctamente a testUser", result.get("message"));
    }

    @Test
    void testRecoverPasswordUserNotFound() {
        // Configuración del mock cuando el usuario no se encuentra
        String username = "nonExistingUser";

        Mockito.when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Verificación de la excepción lanzada
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.recoverPassword(username));

        // Verificación de que el servicio de envío de correo no se invoca
        try {
            Mockito.verify(emailService, Mockito.never()).sendMail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAuthenticatedUserId() {
        // Configuración del mock
        UserEntityUserDetails userDetails = Mockito.mock(UserEntityUserDetails.class);
        SellerRefactor sellerRefactor = new SellerRefactor();
        sellerRefactor.setId(1L);
        Employee employee = new Employee();
        employee.setSellerRefactor(sellerRefactor);

        // Configuración de SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUserEntity()).thenReturn(employee);

        // Llamada al método que deseas probar
        Long result = userEntityServiceImpl.getAuthenticatedUserId();

        // Verificaciones
        Assertions.assertEquals(1L, result);
    }

    @Test
    void testGetAuthenticatedUserIdNoAuthentication() {
        // Configuración de SecurityContextHolder sin autenticación
        SecurityContextHolder.clearContext();

        // Llamada al método que deseas probar
        Long result = userEntityServiceImpl.getAuthenticatedUserId();

        // Verificaciones
        Assertions.assertNull(result);
    }

}
