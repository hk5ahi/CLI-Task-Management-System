package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.User;
import server.dto.UserDTO;
import server.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private final EmployeeDao employeeDao;
    private final ManagerDao managerDao;
    private final UserDao userDao;


    public UserServiceImpl(EmployeeDao employeeDao, ManagerDao managerDao, UserDao userDao) {
        this.employeeDao = employeeDao;
        this.managerDao = managerDao;
        this.userDao = userDao;
    }

    //getByUsernameAndPassword(): Optional<User> 
    @Override
    public Optional<String> verifyUser(String providedUsername, String providedPassword) {
        Optional<User> userOptional = userDao.getByUsername(providedUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Get the User object from the Optional
            if (user.getPassword().equals(providedPassword)) {
                return Optional.of(user.getUserRole()); // Return the current user's role
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
    //method return type might be void
    @Override
    public String createUser(String userRole, String firstname, String lastname, String username, String password) {

        if(userDao.userExist(username))
        {
            //throw exception here
            return "error";
        }
        if (userRole.equals("Employee")) {

            Employee employee=employeeDao.createEmployee(firstname, lastname, username, password);
            userDao.addUser(employee);
            return "success";


        } else if (userRole.equals("Manager")) {

           Manager manager= managerDao.createManager(firstname, lastname, username, password);
           userDao.addUser(manager);
            return "success";


        }
        //throw exception here
        else {
            return "error";
        }



    }
}