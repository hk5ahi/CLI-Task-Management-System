package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.UserDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.domain.Supervisor;
import server.domain.User;
import server.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();
    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    private final UserDao userDao;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Optional<String> verifyUser(String providedUsername, String providedPassword) {
        List<User> users = userDao.getallUsers();

        for (User user : users) {
            if (user.getUsername().equals(providedUsername) && user.getPassword().equals(providedPassword)) {
                return Optional.of(user.getUserRole()); // Return the current user's role
            }
        }

        return Optional.empty(); // Return an empty Optional if user is not found
    }


    @Override
    public List<User> allUsers()
    {
        return userDao.getallUsers();

    }
    @Override
    public String createUser(String userRole, String firstname, String lastname, String username, String password) {

        if (userRole.equals("Employee")) {
            employeeDao.createEmployee(firstname, lastname, username, password);
            return "success";


        } else if (userRole.equals("Manager")) {

            managerDao.createManager(firstname, lastname, username, password);
            return "success";


        }
        else {
            return "error";
        }



    }
}









