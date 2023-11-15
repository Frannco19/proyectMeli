package com.msmeli.service.implement;

import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.repository.RoleRepository;
import com.msmeli.util.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleEntityService implements com.msmeli.service.services.RoleEntityService {

    private final RoleRepository roleRepository;

    public RoleEntityService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity findByName(Role rol) throws ResourceNotFoundException {
        return roleRepository.findByName(rol)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
}
