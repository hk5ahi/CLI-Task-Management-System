package server.service;

import server.domain.Employee;
import server.domain.User;

public interface EmployeeService {


    void initializeEmployee(Employee employee);

    void addTotaltime(int time);

    Employee findEmployee(String providedUsername, String providedPassword);

    User getEmployeeByName(String name);

    void viewAllEmployees();




}
