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

    @Test
    void testUpdateEmployee() {
        // Arrange
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

        // Act
        assertDoesNotThrow(() -> employeeService.updateEmployee(employeeId, requestDTO));

        // Assert
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(userEntityRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(passwordEncoder, times(1)).encode(requestDTO.getPassword());
        verify(employeeRepository, times(1)).save(existingEmployee);
        verify(mapper, times(1)).map(existingEmployee, UserResponseDTO.class);
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        Long employeeId = 1L;
        Employee employeeToDelete = new Employee();

        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employeeToDelete));

        // Act
        assertDoesNotThrow(() -> employeeService.deleteEmployee(employeeId));

        // Assert
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(employeeToDelete);
    }
}
