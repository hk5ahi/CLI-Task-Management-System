package server.service.Implementation;

import server.dao.implementation.EmployeeDaoImpl;
import server.domain.Employee;
import server.domain.Task;
import server.domain.User;
import server.service.EmployeeService;
import server.utilities.Taskbytitle;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();


    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {
        return employeeDao.getEmployees().stream()
                .filter(employee -> employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword))
                .findFirst()
                .orElse(null);
    }



    @Override
    public void initializeEmployee(Employee employee) {
        employee.setFirstName("Muhammad");
        employee.setLastName("Hanan");
        employee.setUsername("m.hanan");
        employee.setPassword("Ts12");
        employee.setUserRole("Employee");
        employeeDao.addEmployee(employee);
    }

    @Override
    public void addTotaltime(int time) {

        Taskbytitle taskbytitle = new Taskbytitle();
        Task task = taskbytitle.gettaskbytitle();
        task.setTotal_time(time);
        System.out.println("The total time has been added successfully.");

    }


    @Override
    public User getEmployeeByName(String name) {
        for (Employee employee : employeeDao.getEmployees()) {
            String employeename = employee.getFirstName() + " " + employee.getLastName();
            if (employeename.equalsIgnoreCase(name)) {
                Employee theemployee = new Employee();
                theemployee.setFirstName(employee.getFirstName());
                theemployee.setLastName(employee.getLastName());
                return theemployee;
            }
        }
        return null; // Employee not found
    }


    @Override
    public void viewAllEmployees() {
        System.out.println("The Employees of the System are:");

        for (Employee employee : employeeDao.getEmployees()) {
            System.out.println(employee.getFirstName() + " " + employee.getLastName());
        }
    }


}



