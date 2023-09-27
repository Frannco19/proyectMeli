package com.msmeli.service.implement;

import com.msmeli.dto.request.UpdatePassRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.IEmailService;
import com.msmeli.service.services.IRoleEntityService;
import com.msmeli.service.services.IUserEntityService;
import com.msmeli.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserEntityService implements IUserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final IRoleEntityService roleEntityService;
    private final IEmailService emailService;


    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, IRoleEntityService roleEntityService, IEmailService emailService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleEntityService = roleEntityService;
        this.emailService = emailService;
    }

    @Override
    public UserResponseDTO create(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException {
        if (userEntityRepository.findByUsername(userRegisterRequestDTO.getUsername()).isPresent())
            throw new AlreadyExistsException("Username already taken");
        if (!userRegisterRequestDTO.getPassword().equals(userRegisterRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Passwords don't match");
        UserEntity userEntity = mapper.map(userRegisterRequestDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userRegisterRequestDTO.getPassword()));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityService.findByName(Role.USER));
        userEntity.setRoles(roles);
        emailService.sendMail(userEntity.getEmail(), "Welcome to MoroTech App", emailWelcomeBody(userEntity.getUsername()));
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

    @Override
    public UserAuthResponseDTO findByUsername(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        return mapper.map(userSearch, UserAuthResponseDTO.class);
    }

    public String recoverPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        emailService.sendMail(userSearch.get().getEmail(), "Recuperar contraseña", emailRecoverPassword(username));
        return "Recovery password email sent successfully to " + username;
    }

    public String resetPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        String newPassword = String.valueOf(UUID.randomUUID()).substring(0, 7);
        userSearch.get().setPassword(passwordEncoder.encode(newPassword));
        emailService.sendMail(userSearch.get().getEmail(), "Restablecer la contraseña", emailResetPassword(username, newPassword));
        userEntityRepository.save(userSearch.get());
        return "\n" +
                "Correo electrónico para restablecer la contraseña enviado correctamente a" + username;
    }

    @Override
    public String updatePassword(UpdatePassRequestDTO updatePassRequestDTO, String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        if (!updatePassRequestDTO.getPassword().equals(updatePassRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Passwords don't match");
        UserEntity userEntity = userSearch.get();
        userEntity.setPassword(passwordEncoder.encode(updatePassRequestDTO.getPassword()));
        userEntityRepository.save(userEntity);
        return "Password updated Successfully";
    }

    private String emailWelcomeBody(String username) {
        return "Dear " + username + ",\n \n" + "Para iniciar sesión click aqui : http://localhost:4200/auth/login/" + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailRecoverPassword(String username) {
        return "Dear " + username + ",\n \n" + "Para continuar con el restablecimiento de su contraseña haga click aquí: http://localhost:4200/recover-password/" + username + "\n \n" + "Si no has solicitado el restablecimiento descarta este correo. " + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailResetPassword(String username, String newPassword) {
        return "Dear " + username + ",\n \n" +
                "Restablecimiento de contraseña exitoso." + "\n \n"+
                "Tu nueva contraseña es :  " + newPassword + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }
}
