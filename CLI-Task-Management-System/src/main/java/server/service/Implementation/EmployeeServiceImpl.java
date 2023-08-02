package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.UserDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.domain.Employee;
import server.domain.Task;
import server.domain.User;
import server.service.EmployeeService;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {

//    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();

    private final EmployeeDao employeeDao;
    private final UserDao userDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao, UserDao userDao) {
        this.employeeDao = employeeDao;
        this.userDao = userDao;
    }

    @Override
    public Employee findEmployee(String providedUsername, String providedPassword) {
        return employeeDao.getEmployees().stream()
                .filter(employee -> employee.getUsername().equals(providedUsername) && employee.getPassword().equals(providedPassword))
                .findFirst()
                .orElse(null);
    }



    @Override
    public void initializeEmployee()
    {
        Employee employee=new Employee();
        employee.setFirstName("Muhammad");
        employee.setLastName("Hanan");
        employee.setUsername("m.hanan");
        employee.setPassword("Ts12");
        employee.setUserRole("Employee");
        employeeDao.addEmployee(employee);
        userDao.addUser(employee);

    }

    @Override
    public void addTotaltime(double time, Task task) {


        if (task == null) {
            System.out.println("There are no tasks to set total time.");
            return;
        }

        task.setTotal_time(time);
        System.out.println("The total time has been added successfully.");
    }


    @Override
    public User getEmployeeByName(String name) {
        if(name!=null) {
            for (Employee employee : employeeDao.getEmployees()) {
                String employeename = employee.getFirstName() + " " + employee.getLastName();
                if (employeename.equalsIgnoreCase(name)) {

                    return employee;
                }
            }
        }
        return null; // Employee not found
    }


    @Override
    public List<Employee> getAllEmployees() {
//        System.out.println("The Employees of the System are:");
//
//        for (Employee employee : employeeDao.getEmployees()) {
//            System.out.println(employee.getFirstName() + " " + employee.getLastName());
//        }

        return employeeDao.getEmployees();
    }


}



