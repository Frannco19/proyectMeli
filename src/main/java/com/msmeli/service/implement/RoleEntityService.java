package com.msmeli.service.implement;

import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.repository.RoleRepository;
import com.msmeli.util.Role;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleEntityService implements com.msmeli.service.services.RoleEntityService {

    private final RoleRepository roleRepository;

    public RoleEntityService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createRoles() {
        if (roleRepository.findByName(Role.ADMIN).isEmpty()) {
            for (Role role : Role.values()) {
                roleRepository.save(RoleEntity.builder().name(role).build());
            }
        }
    }

    public RoleEntity findByName(Role rol) throws ResourceNotFoundException {
        Optional<RoleEntity> role = roleRepository.findByName(rol);
        if (role.isEmpty()) throw new ResourceNotFoundException("Role not found");
        return role.get();
    }
}
