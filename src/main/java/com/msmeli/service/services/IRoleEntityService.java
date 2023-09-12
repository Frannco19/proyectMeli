package com.msmeli.service.services;

import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.util.Role;

public interface IRoleEntityService {
    RoleEntity findByName(Role rol) throws ResourceNotFoundException;
}
