package com.demo.employee.controller;

import com.demo.employee.dto.EmployeeDto;
import com.demo.employee.entity.Employee;
import com.demo.employee.exceptions.EmployeeNotFoundException;
import com.demo.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/swagger")
    public ResponseEntity<List<EmployeeDto>> home(Model model){
        List<EmployeeDto> employeeList=employeeService.employeeList();
        if(employeeList.isEmpty()){
            throw  new EmployeeNotFoundException("No employees found");
        }
        model.addAttribute("employeelist",employeeList);
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @PostMapping("/swagger/saveEmployee")
    public ResponseEntity<String> newEmployee(@Valid @ModelAttribute("employee")EmployeeDto employeeDto, BindingResult result, Model model){
        Employee existingEmployee= employeeService.findByEmail(employeeDto.getEmail());
        if(existingEmployee!=null && existingEmployee.getEmail()!=null && !existingEmployee.getEmail().isEmpty()){
            result.rejectValue("email",null,"Email already exists");
        }
        if (result.hasErrors()){
            model.addAttribute("employee",employeeDto);
            return new ResponseEntity<>("email already exist",HttpStatus.BAD_REQUEST);
        }
        employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>("saved",HttpStatus.CREATED);
    }
    @GetMapping("/swagger/displayEmployee")
    public ResponseEntity<List<EmployeeDto>> searchEmployee(@ModelAttribute("employee") EmployeeDto employeeDto,BindingResult result, Model model){
        String firstName=employeeDto.getFirstName();
        List<EmployeeDto> employee=employeeService.findByName(firstName);
        if(employee==null){
            result.rejectValue("firstName", null, "Employee not found");
            model.addAttribute("errorMessage", "Employee not found");
            return new ResponseEntity<>(employee,HttpStatus.NOT_FOUND);
        }
        model.addAttribute("employees",employee);
        return new ResponseEntity<>(employee,HttpStatus.OK);
    }

    @GetMapping("/swagger/view/{id}")
    public ResponseEntity<EmployeeDto> view(@Valid @PathVariable("id") Long id, Model model){
        EmployeeDto employee=employeeService.getById(id);
        model.addAttribute("employee",employee);
        return new ResponseEntity<>(employee,HttpStatus.OK);
    }

    @GetMapping("/swagger/showFormForUpdate/{email}")
    public ResponseEntity<Employee> update(@Valid @PathVariable("email") String email, Model model){
        Employee employee=employeeService.findByEmail(email);
        model.addAttribute("updateEmployee",employee);
        return new ResponseEntity<>(employee,HttpStatus.OK);
    }

    @PostMapping("/swagger/updateEmployee")
    public ResponseEntity<String> updateEmployee(@ModelAttribute("updateEmployee")EmployeeDto employeeDto){
        employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>("updated",HttpStatus.CREATED);
    }

    @GetMapping("/swagger/deleteById/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        employeeService.deleteById(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}

