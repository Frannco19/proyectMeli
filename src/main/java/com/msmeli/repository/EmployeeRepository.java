package com.msmeli.repository;

import com.msmeli.dto.response.EmployeesResponseDto;
import com.msmeli.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {


}


