package server.service;

import server.domain.Employee;
import server.domain.Task;

import java.util.List;

import static server.domain.Employee.getEmployees;

public interface EmployeeService {


    void empCheck();

    static Employee findEmployee(String providedUsername, String providedPassword) {
        if (getEmployees().size() != 0) {
            List<Employee> employees = (List<Employee>) getEmployees();
            for (Employee employee : employees) {

                if (employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword)) {
                    return employee; // Return the matched server.domain.Employee object
                }
            }
            return null; // If no match found, return null
        }
        return null;
    }

    void createdToInProgress();

    void inProgressToInReview();

    Task getTaskByTitle(String title);

    void addComments(String message);

    void viewAssignedTasks();

    void AllTasks();

    // Other methods if needed
}
