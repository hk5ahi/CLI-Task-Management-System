package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;

import server.dao.SupervisorDao;
import server.dao.UserDao;
import server.dao.implementation.*;
import server.service.EmployeeService;
import server.service.Implementation.EmployeeServiceImpl;
import server.service.Implementation.ManagerServiceImpl;
import server.service.Implementation.SupervisorServiceImpl;
import server.service.Implementation.UserServiceImpl;
import server.service.ManagerService;
import server.service.SupervisorService;
import server.service.UserService;

@Configuration
public class AppConfig {
    @Bean
    public EmployeeDao employeeDao() {
        return EmployeeDaoImpl.getInstance();
    }

    @Bean
    public UserDao userDao() {
        return UserDaoImpl.getInstance();
    }

    @Bean
    public ManagerDao managerDao() {
        return ManagerDaoImpl.getInstance();
    }

    @Bean
    public SupervisorDao supervisorDao() {
        return SupervisorDaoImpl.getInstance();
    }

    @Bean
    public TaskDaoImpl taskDao() {
        return TaskDaoImpl.getInstance();
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeServiceImpl(employeeDao(),userDao());
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userDao());
    }

    @Bean
    public ManagerService managerService() {
        return new ManagerServiceImpl(managerDao(), taskDao(),userDao());
    }

    @Bean
    public SupervisorService supervisorService() {
        return new SupervisorServiceImpl(userDao(),supervisorDao());
    }
}
