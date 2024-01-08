package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.configuration.security.service.JwtService;
import com.msmeli.configuration.security.service.UserEntityRefreshTokenService;
import com.msmeli.dto.request.*;
import com.msmeli.dto.response.UserAuthResponseDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.*;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.EmailService;
import com.msmeli.service.services.RoleEntityService;
import com.msmeli.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final AuthenticationManager authManager;

    private static final String NOT_FOUND = "Usuario no encontrado.";


    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, RoleEntityService roleEntityService, EmailService emailService, UserEntityRefreshTokenService refreshTokenService, JwtService jwtService, SellerRefactorRepository sellerRefactorRepository, EmployeeRepository employeeRepository, AuthenticationManager authManager) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleEntityService = roleEntityService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.employeeRepository = employeeRepository;

        this.authManager = authManager;
    }

    /**
     * Metodo que crea un nuevo vendedor en el sistema.
     *
     * @param userRegisterRequestDTO Datos del usuario para registrar como vendedor.
     * @return UserResponseDTO que representa la información del nuevo vendedor creado.
     * @throws ResourceNotFoundException Si las contraseñas no coinciden.
     * @throws AlreadyExistsException    Si el nombre de usuario ya existe.
     */
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

    /**
     * este metodo crea un nuevo empleado en el sistema asociado a un vendedor existente.
     *
     * @param employeeRegisterDTO Datos del empleado para registrar.
     * @return UserResponseDTO que representa la información del nuevo empleado creado.
     * @throws AlreadyExistsException    Si el nombre de usuario ya existe.
     * @throws ResourceNotFoundException Si las contraseñas ingresadas no coinciden o si no se encuentra el vendedor asociado.
     */
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

    /**
     * Obtiene la información de un usuario por su ID.
     *
     * @param id El ID del usuario a recuperar.
     * @return UserResponseDTO que representa la información del usuario.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el ID proporcionado.
     */
    @Override
    public UserResponseDTO read(Long id) throws ResourceNotFoundException {
        return userEntityRepository.findById(id).map(user -> mapper.map(user, UserResponseDTO.class)).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @return Lista de UserResponseDTO que representa la información de todos los usuarios.
     * @throws ResourceNotFoundException Si no hay usuarios en la base de datos.
     */
    @Override
    public List<UserResponseDTO> readAll() throws ResourceNotFoundException {
        List<UserEntity> usersSearch = userEntityRepository.findAll();
        if (usersSearch.isEmpty()) throw new ResourceNotFoundException("No hay usuarios en la base de datos.");
        return usersSearch.stream().map(userEntity -> mapper.map(userEntity, UserResponseDTO.class)).toList();
    }

    /**
     * Actualiza la información de un usuario en el sistema.
     *
     * @param userEntity La entidad de usuario con la información actualizada.
     * @return La entidad de usuario actualizada.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el ID proporcionado.
     */
    @Override
    public UserEntity update(UserEntity userEntity) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(userEntity.getId());
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        return userEntityRepository.save(userEntity);
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el ID proporcionado.
     */
    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findById(id);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        userEntityRepository.deleteById(id);
    }

    /**
     * Modifica los roles de un usuario en el sistema.
     *
     * @param userId El ID del usuario cuyos roles se van a modificar.
     * @return UserResponseDTO que representa la información del usuario después de la modificación.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el ID proporcionado.
     */
    @Override
    public UserResponseDTO modifyUserRoles(Long userId) throws ResourceNotFoundException {
        UserEntity userEntity = userEntityRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
        RoleEntity admin = roleEntityService.findByName(Role.ADMIN);
        if (userEntity.getRoles().size() == 1) userEntity.getRoles().add(admin);
        else userEntity.getRoles().remove(admin);
        return mapper.map(userEntityRepository.save(userEntity), UserResponseDTO.class);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return UserAuthResponseDTO que representa la información del usuario encontrado.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    public UserAuthResponseDTO findByUsername(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        return mapper.map(userSearch, UserAuthResponseDTO.class);
    }

    /**
     * Inicia el proceso de recuperación de contraseña para un usuario.
     *
     * @param username El nombre de usuario del usuario para el cual se recuperará la contraseña.
     * @return Un mapa con un mensaje indicando que el correo electrónico de recuperación de contraseña se ha enviado correctamente.
     * @throws ResourceNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    public Map<String, String> recoverPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException(NOT_FOUND);
        emailService.sendMail(userSearch.get().getEmail(), "Recuperar contraseña", emailRecoverPassword(username));
        return Map.of("message", "Correo electrónico de recuperación de contraseña enviado correctamente a " + username);
    }

    /**
     * Reinicia la contraseña para un usuario identificado por el nombre de usuario proporcionado.
     *
     * @param username El nombre de usuario del usuario cuya contraseña debe reiniciarse.
     * @return Un Map<String, String> que contiene un mensaje indicando el éxito de la operación de reinicio de contraseña.
     * @throws ResourceNotFoundException Si no se encuentra al usuario con el nombre de usuario especificado.
     */
    public Map<String, String> resetPassword(String username) throws ResourceNotFoundException {
        Optional<UserEntity> userSearch = userEntityRepository.findByUsername(username);
        if (userSearch.isEmpty()) throw new ResourceNotFoundException("User not found");
        String newPassword = String.valueOf(UUID.randomUUID()).substring(0, 7);
        userSearch.get().setPassword(passwordEncoder.encode(newPassword));
        emailService.sendMail(userSearch.get().getEmail(), "Restablecer la contraseña", emailResetPassword(username, newPassword));
        userEntityRepository.save(userSearch.get());
        return Map.of("message", "Correo electrónico para restablecer la contraseña enviado correctamente a" + username);
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param updatePassRequestDTO Objeto que contiene la información necesaria para la actualización de la contraseña.
     * @param username             El nombre de usuario del usuario cuya contraseña se va a actualizar.
     * @return Un Map<String, String> que contiene un mensaje indicando el éxito de la actualización de la contraseña.
     * @throws ResourceNotFoundException Si el usuario con el nombre de usuario especificado no se encuentra, las nuevas contraseñas no coinciden
     *                                   o la contraseña actual proporcionada no es correcta.
     */
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

    /**
     * ESTE METODO SE VA A ELIMINAR
     * Renueva el token de autenticación del usuario utilizando un token de refresco.
     *
     * @param refreshTokenRequestDTO Objeto que contiene el token de refresco necesario para renovar el token de autenticación.
     * @return Un objeto UserAuthResponseDTO que contiene la información actualizada de autenticación del usuario.
     * @throws ResourceNotFoundException Si el token de refresco no se encuentra en la base de datos.
     */
    @Override
    public UserAuthResponseDTO userRefreshToken(UserRefreshTokenRequestDTO refreshTokenRequestDTO) throws ResourceNotFoundException {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken()).map(UserEntityRefreshToken::getUserEntity).map(userEntity -> new UserAuthResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), jwtService.generateToken(userEntity.getUsername(),userEntity.getId()), refreshTokenRequestDTO.getRefreshToken(),userEntity.getRoles())).orElseThrow(() -> new ResourceNotFoundException("El token de refresco no se encuentra en la base de datos."));
    }

    /**
     * Autentica al usuario utilizando el nombre de usuario y la contraseña proporcionados, y genera un nuevo token de autenticación.
     *
     * @param username El nombre de usuario del usuario que se está autenticando.
     * @param pass     La contraseña del usuario que se está autenticando.
     * @return Un objeto UserAuthResponseDTO que contiene la información de autenticación actualizada del usuario, incluido el nuevo token de autenticación.
     * @throws ResourceNotFoundException Si no se encuentra al usuario con el nombre de usuario proporcionado.
     * @throws AppException              Si hay un error en la autenticación, se lanza una excepción de aplicación con detalles específicos del error.
     */
    @Override
    public UserAuthResponseDTO userAuthenticateAndGetToken(String username,String pass) throws ResourceNotFoundException, AppException {
        try{
            UserAuthResponseDTO userAuthResponseDTO = findByUsername(username);
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username,pass));
            userAuthResponseDTO.setToken(jwtService.generateToken(username,userAuthResponseDTO.getId()));
            return userAuthResponseDTO;
        }catch (Exception e){
            throw new AppException(e.getMessage(),"Error en la autenticacion",403,403);
        }
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

    /**
     * Genera el cuerpo del correo electrónico de bienvenida.
     *
     * @param username El nombre de usuario al que se le da la bienvenida.
     * @return El cuerpo del correo electrónico de bienvenida como una cadena de texto.
     */
    private String emailWelcomeBody(String username) {
        return "Hola " + username + ",\n \n" + "Para iniciar sesión click aqui : https://ml.gylgroup.com/auth/login/" + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    /**
     * Genera el cuerpo del correo electrónico para recuperar la contraseña.
     *
     * @param username El nombre de usuario para el cual se está recuperando la contraseña.
     * @return El cuerpo del correo electrónico de recuperación de contraseña como una cadena de texto.
     */
    private String emailRecoverPassword(String username) {
        return "Hola " + username + ",\n \n" + "Para continuar con el restablecimiento de su contraseña haga click aquí: https://ml.gylgroup.com/recover-password/" + username + "\n \n" + "Si no has solicitado el restablecimiento descarta este correo. " + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }

    /**
     * Genera el cuerpo del correo electrónico para notificar un restablecimiento exitoso de la contraseña.
     *
     * @param username    El nombre de usuario para el cual se restableció la contraseña.
     * @param newPassword La nueva contraseña generada y asignada al usuario.
     * @return El cuerpo del correo electrónico de restablecimiento de contraseña exitoso como una cadena de texto.
     */
    private String emailResetPassword(String username, String newPassword) {
        return "Hola " + username + ",\n \n" + "Restablecimiento de contraseña exitoso." + "\n \n" + "Tu nueva contraseña es :  " + newPassword + "\n \n" + "Saludos, equipo de la 3ra Aceleracion.";
    }
}
