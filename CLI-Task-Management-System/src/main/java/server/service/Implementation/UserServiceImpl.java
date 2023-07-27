package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.dao.implementation.UserDaoImpl;
import server.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();
    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    private UserDaoImpl userDao;

    public UserServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public String printName() {
        return userDao.getName();
    }

    @Override
    public void createUser(String type, String firstname, String lastname, String username, String password) {

        if (type.equals("Employee")) {
            employeeDao.createEmployee(firstname, lastname, username, password);

        } else if (type.equals("Manager")) {

            managerDao.createManager(firstname, lastname, username, password);

        }

    }

}









