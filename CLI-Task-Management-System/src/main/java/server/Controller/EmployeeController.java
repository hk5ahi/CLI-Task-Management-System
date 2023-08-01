package server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.domain.Employee;

import server.service.EmployeeService;

import java.util.List;
@RestController
@RequestMapping("/user")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public List<Employee> getallEmployees()
    {
        return employeeService.getAllEmployees();

    }
}
