package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Supervisor;
import server.domain.User;
import server.dto.UserDTO;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;

import server.service.UserService;
import server.utilities.UtilityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final EmployeeDao employeeDao;
    private final ManagerDao managerDao;
    private final UserDao userDao;
    private final UtilityService utilityService;



    public UserServiceImpl(EmployeeDao employeeDao, ManagerDao managerDao, UserDao userDao, UtilityService utilityService) {
        this.employeeDao = employeeDao;
        this.managerDao = managerDao;
        this.userDao = userDao;
        this.utilityService = utilityService;
    }


    @Override
    public List<User> allUsers() {

            return userDao.findAll();

    }

    @Override
    public void initializeUsers()
    {
        List<User> usersList = Arrays.asList(
                new Supervisor("Muhammad", "Asif", "m.asif", "Ts12", User.UserRole.Supervisor),
                new Employee("Muhammad", "Hanan", "m.hanan", "Ts12", User.UserRole.Employee),
                new Manager("Muhammad", "Ubaid", "m.ubaid", "Ts12", User.UserRole.Manager)
        );


        for (User user : usersList) {
            if (!userDao.existsByUsername(user.getUsername())) {

                userDao.saveAndFlush(user);
            } else {
               throw new BadRequestException();
            }
        }

    }

    @Override
    public List<UserDTO> getAllUserForController(String header)
    {

        if (utilityService.isAuthenticatedSupervisor(header)) {
            return viewAllUsers();
        } else {
            throw new ForbiddenAccessException();
        }

    }
    @Override

    public void postCreateUser(User user,String header)
    {

        if (utilityService.isAuthenticatedSupervisor(header)){
            createUser(user.getUserRole(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getPassword());
        }
        else {
            throw new ForbiddenAccessException();
        }

    }
    @Override
    public List<UserDTO> viewAllUsers()
    {
        List<UserDTO> userDTOS=new ArrayList<>();

        for(User user:allUsers())
        {
            UserDTO userDTO=new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setUserRole(user.getUserRole());
            userDTOS.add(userDTO);

        }
        return userDTOS;
    }
    @Override
    public void createUser(User.UserRole userRole, String firstname, String lastname, String username, String password) {

        if(userDao.existsByUsername(username))
        {
            throw new BadRequestException();
        }
        else if (userRole.equals(User.UserRole.Employee)) {

            Employee employee=employeeDao.createEmployee(firstname, lastname, username, password);
            userDao.saveAndFlush(employee);

        } else if (userRole.equals(User.UserRole.Manager)) {

           Manager manager= managerDao.createManager(firstname, lastname, username, password);
           userDao.saveAndFlush(manager);

        }
        else {
            throw new BadRequestException();
        }

    }
}