package com.msmeli.service.implement;

import static com.msmeli.util.Role.SELLER;
import static org.junit.jupiter.api.Assertions.*;

import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.repository.RoleRepository;
import com.msmeli.util.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        roleEntity.setName(Role.SELLER);

        Mockito.when(roleRepository.findByName(role)).thenReturn(Optional.of(roleEntity));


        RoleEntity result = null;
        try {
            result = roleEntityService.findByName(role);
        } catch (ResourceNotFoundException e) {
            Assertions.fail("No debería lanzar una excepción en este caso");
        }


        Mockito.verify(roleRepository, Mockito.atLeastOnce()).findByName(role);

        Assertions.assertEquals("SELLER", result.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        Role role = Role.SELLER;

        Mockito.when(roleRepository.findByName(role)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> roleEntityService.findByName(role));

        Mockito.verify(roleRepository, Mockito.atLeastOnce()).findByName(role);
    }
    @Test
    public void testFindByNameWhenRoleNotFound() {
        Role role = null ;
        Mockito.when(roleRepository.findByName(role)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> roleEntityService.findByName(role));

        Mockito.verify(roleRepository, Mockito.atLeastOnce()).findByName(role);
    }

}
