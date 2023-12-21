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

        Role role = Role.SELLER;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(SELLER);

        when(roleRepository.findByName(role)).thenReturn(Optional.of(roleEntity));


        RoleEntity result = null;
        try {
            result = roleEntityService.findByName(role);
        } catch (ResourceNotFoundException e) {
            fail("No debería lanzar una excepción en este caso");
        }


        verify(roleRepository, atLeastOnce()).findByName(role);

        assertEquals("SELLER", result.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        // Configurar comportamiento simulado para el mock cuando no se encuentra el rol
        Role role = Role.SELLER;

        when(roleRepository.findByName(role)).thenReturn(Optional.empty());

        // Llamar al método que estás probando y esperar la excepción
        assertThrows(ResourceNotFoundException.class, () -> roleEntityService.findByName(role));

        // Verificar que los métodos necesarios se hayan llamado
        verify(roleRepository, atLeastOnce()).findByName(role);
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

}
