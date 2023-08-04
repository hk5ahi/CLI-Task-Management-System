package server.service;

import org.springframework.http.ResponseEntity;
import server.domain.Employee;
import server.domain.Task;
import server.domain.User;

import java.util.List;

public interface EmployeeService {


    void initializeEmployee();

    ResponseEntity<String> addTotaltime(double time, String title, Employee employee);

    Employee findEmployee(String providedUsername, String providedPassword);

    User getEmployeeByName(String name);

    List<Employee> getAllEmployees();


}
