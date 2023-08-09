package server.service.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.User;
import server.dto.UserDTO;
import server.exception.BadRequestException;
import server.exception.ForbiddenAccessException;
import server.exception.StatusCREATEDMessage;
import server.service.UserService;
import server.utilities.UtilityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    //getByUsername(): Optional<> 
    @Override
    public Optional<User> getUserByNameAndPassword(String providedUsername, String providedPassword) {
        Optional<User> userOptional = userDao.getByUsername(providedUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Get the User object from the Optional
            if (user.getPassword().equals(providedPassword)) {
                return Optional.of(user); // Return the current user's role
            }
        }

        return Optional.empty(); // Return an empty Optional if user is not found or password doesn't match
    }



    @Override
    public List<User> allUsers()
    {
        return userDao.getAllUsers();

    }

    @Override
    public void initializeUsers()
    {
        userDao.initializeUsers();

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

        if(userDao.userExist(username))
        {
            throw new BadRequestException();
        }
        else if (userRole.equals(User.UserRole.Employee)) {

            Employee employee=employeeDao.createEmployee(firstname, lastname, username, password);
            userDao.addUser(employee);

        } else if (userRole.equals(User.UserRole.Manager)) {

           Manager manager= managerDao.createManager(firstname, lastname, username, password);
           userDao.addUser(manager);

        }
        else {
            throw new BadRequestException();
        }

    }
}