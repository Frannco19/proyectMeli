package com.msmeli.service.implement;

import static com.msmeli.util.Role.SELLER;
import static org.junit.jupiter.api.Assertions.*;

import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.repository.RoleRepository;
import com.msmeli.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleEntityServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntityService roleEntityService;

    @Test
    public void testFindByName() {
        // Configurar comportamiento simulado para el mock
        Role role = Role.SELLER;  // O el valor que desees probar

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(SELLER);  // O el nombre que corresponde al rol

        when(roleRepository.findByName(role)).thenReturn(Optional.of(roleEntity));

        // Llamar al método que estás probando
        RoleEntity result = null;
        try {
            result = roleEntityService.findByName(role);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Verificar que los métodos necesarios se hayan llamado
        verify(roleRepository, atLeastOnce()).findByName(role);

        // Realizar aserciones
        assertEquals("SELLER", result.getName());  // O el valor esperado según tu lógica
    }

    @Test
    public void testFindByNameWhenRoleNotFound() {
        // Configurar comportamiento simulado para el mock cuando no se encuentra el rol
        Role role = null /* Crear un objeto Role según tus necesidades */;
        when(roleRepository.findByName(role)).thenReturn(Optional.empty());

        // Llamar al método que estás probando y verificar que se lance la excepción esperada
        assertThrows(ResourceNotFoundException.class, () -> roleEntityService.findByName(role));

        // Verificar que los métodos necesarios se hayan llamado
        verify(roleRepository, atLeastOnce()).findByName(role);
    }

    // Puedes seguir escribiendo más pruebas para otros métodos de RoleEntityService
}
