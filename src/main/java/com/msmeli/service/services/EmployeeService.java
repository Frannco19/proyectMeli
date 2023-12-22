package com.msmeli.service.services;

import com.msmeli.dto.request.EmployeeUpdateRequestDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    UserResponseDTO updateEmployee(Long employeeId, EmployeeUpdateRequestDTO employeeUpdateDTO) throws ResourceNotFoundException, AlreadyExistsException;

    void deleteEmployee(Long employeeId) throws ResourceNotFoundException;

}
