package com.msmeli.service.implement;

import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.IRoleEntityService;
import com.msmeli.service.services.IUserEntityService;
import com.msmeli.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityService implements IUserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final IRoleEntityService roleEntityService;


    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, IRoleEntityService roleEntityService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleEntityService = roleEntityService;
    }

    @Override
    public UserResponseDTO create(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException {
        if (!userRegisterRequestDTO.getPassword().equals(userRegisterRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Passwords don't match");
        UserEntity userEntity = mapper.map(userRegisterRequestDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityService.findByName(Role.USER));
        userEntity.setRoles(roles);
        return mapper.map(userEntityRepository.save(userEntity), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO read(Long id) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(id);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        return mapper.map(userSearch.get(), UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> readAll() throws ResourceNotFoundException {
        List<UserEntity> usersSearch = userEntityRepository.findAll();
        if (usersSearch.isEmpty()) throw new ResourceNotFoundException("No users found");
        return usersSearch.stream().map(userEntity -> mapper.map(userEntity, UserResponseDTO.class)).toList();
    }

    @Override
    public UserEntity update(UserEntity userEntity) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(userEntity.getId());
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        return userEntityRepository.save(userEntity);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(id);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        userEntityRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException {
        Optional<UserEntity> user = userEntityRepository.findById(userId);
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found");
        UserEntity userEntity = user.get();
        RoleEntity admin = roleEntityService.findByName(Role.ADMIN);
        if (userEntity.getRoles().size() == 1) userEntity.getRoles().add(admin);
        else userEntity.getRoles().remove(admin);
        return mapper.map(userEntityRepository.save(userEntity), UserResponseDTO.class);
    }
}
