package com.msmeli.service.implement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.dto.request.EmployeeUpdateRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.model.Employee;
import com.msmeli.repository.EmployeeRepository;
import com.msmeli.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Prueba la funcionalidad de "updateEmployee" en el servicio de empleados (EmployeeService).
     * Se configura un escenario donde se proporciona un ID de empleado y una solicitud de actualización (EmployeeUpdateRequestDTO).
     * Se simula el comportamiento del repositorio de empleados al devolver un empleado existente con el ID proporcionado.
     * Se simula también el comportamiento del repositorio de entidades de usuario al no encontrar un usuario con el nuevo nombre de usuario.
     * Se simula la codificación de la nueva contraseña y la devolución de una respuesta de usuario mapeada.
     * Luego, se llama al método "updateEmployee" del servicio de empleados y se verifica que las interacciones esperadas
     * con los repositorios, el codificador de contraseña y el mapeador hayan ocurrido sin errores.
     */
    @Test
    void testUpdateEmployee() {
        Long employeeId = 1L;
        EmployeeUpdateRequestDTO requestDTO = new EmployeeUpdateRequestDTO();
        requestDTO.setUsername("newUsername");
        requestDTO.setPassword("newPassword");
        requestDTO.setEmail("newEmail");
        requestDTO.setNombre("newNombre");
        requestDTO.setApellido("newApellido");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setUsername("oldUsername");
        existingEmployee.setPassword("oldPassword");
        existingEmployee.setEmail("oldEmail");
        existingEmployee.setName("oldNombre");
        existingEmployee.setLastname("oldApellido");

        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(existingEmployee));
        when(userEntityRepository.findByUsername(requestDTO.getUsername())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(mapper.map(existingEmployee, UserResponseDTO.class)).thenReturn(new UserResponseDTO());

        assertDoesNotThrow(() -> employeeService.updateEmployee(employeeId, requestDTO));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(userEntityRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(passwordEncoder, times(1)).encode(requestDTO.getPassword());
        verify(employeeRepository, times(1)).save(existingEmployee);
        verify(mapper, times(1)).map(existingEmployee, UserResponseDTO.class);
    }

    /**
     * Prueba la funcionalidad de "deleteEmployee" en el servicio de empleados (EmployeeService).
     * Se configura un escenario donde se proporciona un ID de empleado y se simula el comportamiento del repositorio de empleados
     * al devolver un empleado existente con el ID proporcionado. Luego, se llama al método "deleteEmployee" del servicio de empleados
     * y se verifica que las interacciones esperadas con el repositorio hayan ocurrido sin errores, incluida la llamada al método "delete".
     */
    @Test
    void testDeleteEmployee() {
        Long employeeId = 1L;
        Employee employeeToDelete = new Employee();

        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employeeToDelete));

        assertDoesNotThrow(() -> employeeService.deleteEmployee(employeeId));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(employeeToDelete);
    }
}
