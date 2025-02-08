package com.demo.employee.service.implementation;

import com.demo.employee.dto.EmployeeDto;
import com.demo.employee.entity.Employee;
import com.demo.employee.exceptions.ResourceNotFoundException;
import com.demo.employee.repository.EmployeeRepository;
import com.demo.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EmployeeDto> employeeList() {
        List<Employee> employeeList=employeeRepository.findAll();
        return employeeList.stream().map(employee -> modelMapper.map(employee,EmployeeDto.class)).collect(Collectors.toList());
    }
    @Override
    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee=modelMapper.map(employeeDto,Employee.class);
        employeeRepository.save(employee);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<EmployeeDto> findByName(String firstName) {
        List<Employee> employee=employeeRepository.findByFirstName(firstName);
        if (employee.isEmpty()){
            return null;
        }
        return employee.stream().map(employees -> modelMapper.map(employees,EmployeeDto.class)).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getById(Long id) {
        return modelMapper.map(employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee","id",id)), EmployeeDto.class);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
