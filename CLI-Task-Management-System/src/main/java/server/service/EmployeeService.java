package server.service;

import server.domain.Employee;
import server.domain.Task;
import server.domain.User;

import java.util.List;

public interface EmployeeService {


    void initializeEmployee();

    void addTotaltime(double time, Task task);

    Employee findEmployee(String providedUsername, String providedPassword);

    User getEmployeeByName(String name);

    List<Employee> getAllEmployees();


}
