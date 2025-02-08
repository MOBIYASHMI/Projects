package com.demo.employee.service;

import com.demo.employee.dto.EmployeeDto;
import com.demo.employee.entity.Employee;

import java.util.List;

public interface EmployeeService {

    //  READ: Employee List
    List<EmployeeDto> employeeList();

//  CREATE
    void saveEmployee(EmployeeDto employeeDto);

    Employee findByEmail(String email);

    List<EmployeeDto> findByName(String name);

//  getById
    EmployeeDto getById(Long id);

//  DELETE
    void deleteById(Long id);


}
