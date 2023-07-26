package server.service.Implementation;

import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.service.UserService;

public class UserServiceImpl implements UserService {

    private EmployeeDaoImpl employeeDao = EmployeeDaoImpl.getInstance();
    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();

    public void createUser(String type, String firstname, String lastname, String username, String password) {

        if (type.equals("Employee")) {
            employeeDao.createEmployee(firstname, lastname, username, password);

        } else if (type.equals("Manager")) {

            managerDao.createManager(firstname, lastname, username, password);

        }

    }

}









