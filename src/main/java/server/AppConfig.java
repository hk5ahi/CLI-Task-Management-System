package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.dao.*;

import server.dao.implementation.*;
import server.service.*;
import server.service.Implementation.*;

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
    public CommentDaoImpl commentDao() {
        return CommentDaoImpl.getInstance();
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeServiceImpl(employeeDao(),userDao(), taskDao());
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
