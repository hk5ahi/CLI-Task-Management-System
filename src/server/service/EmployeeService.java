package server.service;
import server.domain.Employee;
public interface EmployeeService {


    void employeeCheck();

    Employee findEmployee(String providedUsername, String providedPassword);

    String getEmployeeByName(String name);

    void viewAllEmployees();

    void createdToInProgress(Employee employee);

    void inProgressToInReview(Employee employee);

    void createEmployee(String firstname, String lastname, String username, String password);

}
