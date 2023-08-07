package server.service;

import org.springframework.http.ResponseEntity;
import server.domain.Employee;


import java.util.List;

public interface EmployeeService {


    ResponseEntity<String> addTotalTime(double time, String title, Employee employee);

    List<Employee> getAllEmployees();


}
