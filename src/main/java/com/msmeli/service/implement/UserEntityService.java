package com.msmeli.service.implement;

import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityRefreshTokenService;
import com.msmeli.dto.request.UpdatePassRequestDTO;
import com.msmeli.dto.request.UserRefreshTokenRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.UserEntity;
import com.msmeli.model.UserEntityRefreshToken;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.IEmailService;
import com.msmeli.service.services.IRoleEntityService;
import com.msmeli.service.services.IUserEntityService;
import com.msmeli.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserEntityService implements IUserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final IRoleEntityService roleEntityService;
    private final IEmailService emailService;
    private final UserEntityRefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private static final String NOT_FOUND = "Usuario no encontrado.";


    public UserEntityService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, IRoleEntityService roleEntityService, IEmailService emailService, UserEntityRefreshTokenService refreshTokenService, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleEntityService = roleEntityService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO create(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException {
        if (!userRegisterRequestDTO.getPassword().equals(userRegisterRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Las contraseñas ingresadas no coinciden.");
        if (userEntityRepository.findByUsername(userRegisterRequestDTO.getUsername()).isPresent())
            throw new AlreadyExistsException("El nombre de usuario ya existe.");
        UserEntity userEntity = mapper.map(userRegisterRequestDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userRegisterRequestDTO.getPassword()));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityService.findByName(Role.USER));
        userEntity.setRoles(roles);
        UserEntity savedUser = userEntityRepository.save(userEntity);
        refreshTokenService.createRefreshToken(savedUser);
        emailService.sendMail(userEntity.getEmail(), "Bienvenido a G&L App", emailWelcomeBody(userEntity.getUsername()));
        return mapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO read(Long id) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(id);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        return mapper.map(userSearch.get(), UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> readAll() throws ResourceNotFoundException {
        List<UserEntity> usersSearch = userEntityRepository.findAll();
        if (usersSearch.isEmpty()) throw new ResourceNotFoundException("No hay usuarios en la base de datos.");
        return usersSearch.stream().map(userEntity -> mapper.map(userEntity, UserResponseDTO.class)).toList();
    }

    @Override
    public UserEntity update(UserEntity userEntity) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(userEntity.getId());
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        return userEntityRepository.save(userEntity);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(id);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        userEntityRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException {
        Optional<UserEntity> user = userEntityRepository.findById(userId);
        if (user.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        UserEntity userEntity = user.get();
        RoleEntity admin = roleEntityService.findByName(Role.ADMIN);
        if (userEntity.getRoles().size() == 1) userEntity.getRoles().add(admin);
        else userEntity.getRoles().remove(admin);
        return mapper.map(userEntityRepository.save(userEntity), UserResponseDTO.class);
    }

    @Override
    public UserAuthResponseDTO findByUsername(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        return mapper.map(userSearch, UserAuthResponseDTO.class);
    }

    public Map<String, String> recoverPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        emailService.sendMail(userSearch.get().getEmail(), "Recuperar contraseña", emailRecoverPassword(username));
        return Map.of("message", "Correo electrónico de recuperación de contraseña enviado correctamente a " + username);
    }

    public Map<String, String> resetPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        String newPassword = String.valueOf(UUID.randomUUID()).substring(0, 7);
        userSearch.get().setPassword(passwordEncoder.encode(newPassword));
        emailService.sendMail(userSearch.get().getEmail(), "Restablecer la contraseña", emailResetPassword(username, newPassword));
        userEntityRepository.save(userSearch.get());
        return Map.of("message", "Correo electrónico para restablecer la contraseña enviado correctamente a" + username);
    }

    @Override
    public Map<String, String> updatePassword(UpdatePassRequestDTO updatePassRequestDTO, String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        if (!updatePassRequestDTO.getPassword().equals(updatePassRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Las nuevas contraseñas no coinciden.");
        UserEntity userEntity = userSearch.get();
        if (!passwordEncoder.matches(updatePassRequestDTO.getCurrentPassword(), userEntity.getPassword()))
            throw new ResourceNotFoundException("La contraseña actual es incorrecta.");
        userEntity.setPassword(passwordEncoder.encode(updatePassRequestDTO.getPassword()));
        userEntityRepository.save(userEntity);
        return Map.of("message", "Contraseña actualizada correctamente.");
    }

    @Override
    public UserAuthResponseDTO userRefreshToken(UserRefreshTokenRequestDTO refreshTokenRequestDTO) throws ResourceNotFoundException {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken()).map(UserEntityRefreshToken::getUserEntity).map(userEntity -> new UserAuthResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), jwtService.generateToken(userEntity.getUsername()), refreshTokenRequestDTO.getRefreshToken())).orElseThrow(() -> new ResourceNotFoundException("El token de refresco no se encuentra en la base de datos."));
    }

    @Override
    public UserAuthResponseDTO userAuthenticateAndGetToken(String username) throws ResourceNotFoundException {
        UserAuthResponseDTO userAuthResponseDTO = findByUsername(username);
        userAuthResponseDTO.setToken(jwtService.generateToken(username));
        userAuthResponseDTO.setRefreshToken(refreshTokenService.findByUsername(userAuthResponseDTO.getUsername()).get().getToken());
        return userAuthResponseDTO;
    }

    private String emailWelcomeBody(String username) {
        return "Hola " + username + ",\n \n" + "Para iniciar sesión click aqui : http://201.216.243.146:10080/auth/login/" + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailRecoverPassword(String username) {
        return "Hola " + username + ",\n \n" + "Para continuar con el restablecimiento de su contraseña haga click aquí: http://201.216.243.146:10080/recover-password/" + username + "\n \n" + "Si no has solicitado el restablecimiento descarta este correo. " + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailResetPassword(String username, String newPassword) {
        return "Hola " + username + ",\n \n" + "Restablecimiento de contraseña exitoso." + "\n \n" + "Tu nueva contraseña es :  " + newPassword + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }
}
