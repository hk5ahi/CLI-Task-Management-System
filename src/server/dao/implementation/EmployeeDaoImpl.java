package server.dao;

import server.domain.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    private List<Employee> employees = new ArrayList<>();

    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username)) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getUsername().equals(employee.getUsername())) {
                employees.set(i, employee);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username)) {
                employees.remove(employee);
                return true;
            }
        }
        return false;
    }
}
