package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.UserDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.domain.Supervisor;
import server.domain.User;
import server.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();
    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    private final UserDao userDao;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public String verifyUser(String providedUsername, String providedPassword) {
        List<User> users = userDao.getallUsers();

        for (int i = 0; i < users.size(); i++) {


            if (users.get(i).getUsername().equals(providedUsername) && users.get(i).getPassword().equals(providedPassword)) {
                return users.get(i).getUserRole(); // Return the current Supervisor object
            }
        }
        return "Not Found";
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









