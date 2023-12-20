package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityRefreshTokenService;
import com.msmeli.dto.request.*;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.*;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.EmailService;
import com.msmeli.service.services.RoleEntityService;
import com.msmeli.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserEntityServiceImpl implements com.msmeli.service.services.UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final RoleEntityService roleEntityService;
    private final EmailService emailService;
    private final UserEntityRefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final SellerRefactorRepository sellerRefactorRepository;

    private final EmployeeRepository employeeRepository;

    private static final String NOT_FOUND = "Usuario no encontrado.";


    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, RoleEntityService roleEntityService, EmailService emailService, UserEntityRefreshTokenService refreshTokenService, JwtService jwtService, SellerRefactorRepository sellerRefactorRepository, EmployeeRepository employeeRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleEntityService = roleEntityService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.employeeRepository = employeeRepository;

    }

    @Override
    public UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException {
        if (!userRegisterRequestDTO.getPassword().equals(userRegisterRequestDTO.getRePassword()))
            throw new ResourceNotFoundException("Las contraseñas ingresadas no coinciden.");
        if (userEntityRepository.findByUsername(userRegisterRequestDTO.getUsername()).isPresent())
            throw new AlreadyExistsException("El nombre de usuario ya existe.");
        SellerRefactor newSeller = mapper.map(userRegisterRequestDTO,SellerRefactor.class);
        newSeller.setPassword(passwordEncoder.encode(userRegisterRequestDTO.getPassword()));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityService.findByName(Role.SELLER));
        newSeller.setRoles(roles);
        sellerRefactorRepository.save(newSeller);
        emailService.sendMail(newSeller.getEmail(),"Bienvenido a G&L App", emailWelcomeBody(newSeller.getUsername()));
        return mapper.map(newSeller, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO createEmployee(EmployeeRegisterRequestDTO employeeRegisterDTO) throws AlreadyExistsException, ResourceNotFoundException {

        if (!employeeRegisterDTO.getPassword().equals(employeeRegisterDTO.getRePassword()))
            throw new ResourceNotFoundException("Las contraseñas ingresadas no coinciden.");
        if (userEntityRepository.findByUsername(employeeRegisterDTO.getUsername()).isPresent())
            throw new AlreadyExistsException("El nombre de usuario ya existe.");

        Long id = getAuthenticatedUserId();

        Employee newEmployee = mapper.map(employeeRegisterDTO, Employee.class);
        newEmployee.setPassword(passwordEncoder.encode(employeeRegisterDTO.getPassword()));
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityService.findByName(Role.EMPLOYEE));
        newEmployee.setRoles(roles);
        SellerRefactor seller = sellerRefactorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el seller en la base de datos"));

        newEmployee.setSellerRefactor(seller);
        newEmployee.setPassword(passwordEncoder.encode(employeeRegisterDTO.getPassword()));
        employeeRepository.save(newEmployee);
        return mapper.map(newEmployee,UserResponseDTO.class);
    }

    @Override
    @Transactional
    public UserResponseDTO updateEmployee(Long employeeId, EmployeeUpdateRequestDTO employeeUpdateDTO)
            throws ResourceNotFoundException, AlreadyExistsException {
        // Obtener el empleado existente por su ID
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Validar si el nuevo nombre de usuario ya existe
        if (!existingEmployee.getUsername().equals(employeeUpdateDTO.getUsername())
                && userEntityRepository.findByUsername(employeeUpdateDTO.getUsername()).isPresent()) {
            throw new AlreadyExistsException("El nombre de usuario ya existe.");
        }

        // Actualizar los campos necesarios
        existingEmployee.setUsername(employeeUpdateDTO.getUsername());
        existingEmployee.setPassword(passwordEncoder.encode(employeeUpdateDTO.getPassword()));
        existingEmployee.setEmail(employeeUpdateDTO.getEmail());
        existingEmployee.setNombre(employeeUpdateDTO.getNombre());
        existingEmployee.setApellido(employeeUpdateDTO.getApellido());
        existingEmployee.setRol(employeeUpdateDTO.getRol());

        // Guardar la entidad actualizada
        employeeRepository.save(existingEmployee);

        // Mapear la entidad a DTO y devolver la respuesta
        return mapper.map(existingEmployee, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO read(Long id) throws ResourceNotFoundException {
        return userEntityRepository.findById(id).map(user -> mapper.map(user, UserResponseDTO.class)).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
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
    public void deleteEmployee(Long employeeId) throws ResourceNotFoundException {
        // Obtener el empleado por su ID
        Employee employeeToDelete = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Eliminar el empleado
        employeeRepository.delete(employeeToDelete);
    }

    @Override
    public UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException {
        UserEntity userEntity = userEntityRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
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
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken()).map(UserEntityRefreshToken::getUserEntity).map(userEntity -> new UserAuthResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), jwtService.generateToken(userEntity.getUsername(),userEntity.getId()), refreshTokenRequestDTO.getRefreshToken(),userEntity.getRoles())).orElseThrow(() -> new ResourceNotFoundException("El token de refresco no se encuentra en la base de datos."));
    }

    @Override
    public UserAuthResponseDTO userAuthenticateAndGetToken(String username) throws ResourceNotFoundException {
        UserAuthResponseDTO userAuthResponseDTO = findByUsername(username);
        userAuthResponseDTO.setToken(jwtService.generateToken(username,userAuthResponseDTO.getId()));

        return userAuthResponseDTO;
    }

    /**
     * Metodo que se encarga de devolver el id del Seller que pidio el recurso
     * se encrga de comprobar si es un instanci de selleo o employee para poder conseguir el id
     * @return Long id recuperado del usuaria que solicita el recurso
     */
    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntityUserDetails) {
            UserEntityUserDetails userDetails = (UserEntityUserDetails) authentication.getPrincipal();
            UserEntity userEntity = userDetails.getUserEntity();
            if (userEntity instanceof Employee){
                return ((Employee) userEntity).getSellerRefactor().getId();
            } else if (userEntity instanceof SellerRefactor){
                return userEntity.getId();
            }
        }
        return null;
    }

    private String emailWelcomeBody(String username) {
        return "Hola " + username + ",\n \n" + "Para iniciar sesión click aqui : https://ml.gylgroup.com/auth/login/" + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailRecoverPassword(String username) {
        return "Hola " + username + ",\n \n" + "Para continuar con el restablecimiento de su contraseña haga click aquí: https://ml.gylgroup.com/recover-password/" + username + "\n \n" + "Si no has solicitado el restablecimiento descarta este correo. " + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    private String emailResetPassword(String username, String newPassword) {
        return "Hola " + username + ",\n \n" + "Restablecimiento de contraseña exitoso." + "\n \n" + "Tu nueva contraseña es :  " + newPassword + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }


}
