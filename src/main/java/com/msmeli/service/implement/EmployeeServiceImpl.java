package com.msmeli.service.implement;

import com.msmeli.dto.request.EmployeeUpdateRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Employee;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.msmeli.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               UserEntityRepository userEntityRepository,
                               PasswordEncoder passwordEncoder,
                               ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

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

        // Actualizar solo los campos proporcionados en la solicitud
        if (employeeUpdateDTO.getUsername() != null) {
            existingEmployee.setUsername(employeeUpdateDTO.getUsername());
        }
        if (employeeUpdateDTO.getPassword() != null) {
            existingEmployee.setPassword(passwordEncoder.encode(employeeUpdateDTO.getPassword()));
        }
        if (employeeUpdateDTO.getEmail() != null) {
            existingEmployee.setEmail(employeeUpdateDTO.getEmail());
        }
        if (employeeUpdateDTO.getNombre() != null) {
            existingEmployee.setName(employeeUpdateDTO.getNombre());
        }
        if (employeeUpdateDTO.getApellido() != null) {
            existingEmployee.setLastname(employeeUpdateDTO.getApellido());
        }

        // Guardar la entidad actualizada
        employeeRepository.save(existingEmployee);

        // Mapear la entidad a DTO y devolver la respuesta
        return mapper.map(existingEmployee, UserResponseDTO.class);
    }


    public void deleteEmployee(Long employeeId) throws ResourceNotFoundException {
        // Obtener el empleado por su ID
        Employee employeeToDelete = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Eliminar el empleado
        employeeRepository.delete(employeeToDelete);
    }
}
