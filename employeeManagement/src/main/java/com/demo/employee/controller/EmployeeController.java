package com.demo.employee.controller;

import com.demo.employee.dto.EmployeeDto;
import com.demo.employee.entity.Employee;
import com.demo.employee.exceptions.EmployeeNotFoundException;
import com.demo.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/")
    public String home(Model model){
        List<EmployeeDto> employeeList=employeeService.employeeList();
        if(employeeList.isEmpty()){
            throw  new EmployeeNotFoundException("No employees found");
        }
        model.addAttribute("employeelist",employeeList);
        return "index";
    }

    @GetMapping("/addNewEmployee")
    public String addNewEmployee(Model model){
        EmployeeDto employee=new EmployeeDto();
        model.addAttribute("employee",employee);
        return "newemployeeform";
    }
    @PostMapping("/saveEmployee")
    public String newEmployee(@Valid @ModelAttribute("employee")EmployeeDto employeeDto, BindingResult result, Model model){
        Employee existingEmployee= employeeService.findByEmail(employeeDto.getEmail());
        if(existingEmployee!=null && existingEmployee.getEmail()!=null && !existingEmployee.getEmail().isEmpty()){
            result.rejectValue("email",null,"Email already exists");
        }
        if (result.hasErrors()){
            model.addAttribute("employee",employeeDto);
            return "newemployeeform";
        }
        employeeService.saveEmployee(employeeDto);
        return "redirect:/?success";
    }
    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("employee",new EmployeeDto());
        return "search";
    }

    @GetMapping("/displayEmployee")
    public String searchEmployee(@ModelAttribute("employee") EmployeeDto employeeDto,BindingResult result, Model model){
        String firstName=employeeDto.getFirstName();
        List<EmployeeDto> employee=employeeService.findByName(firstName);
        if(employee==null){
            result.rejectValue("firstName", null, "Employee not found");
            model.addAttribute("errorMessage", "Employee not found");
            return "search";
        }
        model.addAttribute("employees",employee);
        return "search";
    }

    @GetMapping("/view/{id}")
    public String view(@Valid @PathVariable("id") Long id,Model model){
        EmployeeDto employee=employeeService.getById(id);
        model.addAttribute("employee",employee);
        return "view";
    }

    @GetMapping("/showFormForUpdate/{email}")
    public String update(@Valid @PathVariable("email") String email, Model model){
        Employee employee=employeeService.findByEmail(email);
        model.addAttribute("updateEmployee",employee);
        return "update";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute("updateEmployee")EmployeeDto employeeDto){
        employeeService.saveEmployee(employeeDto);
        return "redirect:/?success";
    }

    @GetMapping("/deleteById/{id}")
    public String delete(@PathVariable Long id){
        employeeService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/documentation")
    public String documentation(){
        return "documentation";
    }
}
