package com.msmeli.service.implement;

import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.RoleEntityService;
import com.msmeli.util.Role;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserEntityServiceImplTest {

    @InjectMocks
    private UserEntityServiceImpl userEntityServiceImpl;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @Mock
    private RoleEntityService roleEntityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /*@Test
    public void testCreateUser() throws ResourceNotFoundException {
        // Definir el comportamiento esperado del repositorio
        UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();
        requestDTO.setUsername("testuser");
        requestDTO.setPassword("password");
        requestDTO.setRePassword("password");

        when(userEntityRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(roleEntityService.findByName(Role.USER)).thenReturn(new RoleEntity());

        UserEntity userEntity = new UserEntity();
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Configurar el comportamiento del password encoder
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Llamar al método y verificar el resultado
        try {
            UserResponseDTO responseDTO = userEntityService.create(requestDTO);
            assertNotNull(responseDTO);
            // Puedes realizar más aserciones según sea necesario
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            fail("Se lanzó una excepción de manera inesperada: " + e.getMessage());
        }

        // Verificar que el método sendMail se haya llamado con los parámetros adecuados
        verify(emailService).sendMail(eq(userEntity.getEmail()), eq("Welcome to MoroTech App"), anyString());
    }*/


    @SneakyThrows
    @Test
    public void testReadUser() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Llamar al método y verificar el resultado
        try {
            UserResponseDTO responseDTO = userEntityServiceImpl.read(1L);

            //assertNotNull(responseDTO);
            //assertEquals(1L, responseDTO.getId());
        } catch (ResourceNotFoundException e) {
            fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @SneakyThrows
    @Test
    public void testReadAllUsers() {
        // Definir el comportamiento esperado del repositorio
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        when(userEntityRepository.findAll()).thenReturn(users);

        // Llamar al método y verificar el resultado
        try {
            List<UserResponseDTO> responseDTOs = userEntityServiceImpl.readAll();

            assertNotNull(responseDTOs);
            assertFalse(responseDTOs.isEmpty());
        } catch (ResourceNotFoundException e) {
            fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @SneakyThrows
    @Test
    public void testUpdateUser() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(userEntity)).thenReturn(userEntity);

        // Llamar al método y verificar el resultado
        try {
            UserEntity updatedUserEntity = userEntityServiceImpl.update(userEntity);

            assertNotNull(updatedUserEntity);
            assertEquals(1L, updatedUserEntity.getId());
        } catch (ResourceNotFoundException e) {
            fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @Test
    public void testDeleteUser() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Llamar al método y verificar que no se lance ninguna excepción
        assertDoesNotThrow(() -> userEntityServiceImpl.delete(1L));
    }

    @SneakyThrows
    @Test
    public void testModifyUserRoles() {
        // Definir el comportamiento esperado del repositorio y otros mocks
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        RoleEntity adminRole = new RoleEntity();
        when(roleEntityService.findByName(Role.ADMIN)).thenReturn(adminRole);

        // Llamar al método y verificar el resultado
        try {
            UserResponseDTO responseDTO = userEntityServiceImpl.modifyUserRoles(1L);

            //assertNotNull(responseDTO);
            assertEquals(1L, responseDTO.getId());
        } catch (ResourceNotFoundException e) {
            fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }

    @SneakyThrows
    @Test
    public void testFindByUsername() {
        // Definir el comportamiento esperado del repositorio
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        when(userEntityRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        // Llamar al método y verificar el resultado
        try {
            UserAuthResponseDTO responseDTO = userEntityServiceImpl.findByUsername("testuser");

            //assertNotNull(responseDTO);
            //assertEquals("testuser", responseDTO.getUsername());
        } catch (ResourceNotFoundException e) {
            fail("Se lanzó una ResourceNotFoundException de manera inesperada.");
        }
    }
}