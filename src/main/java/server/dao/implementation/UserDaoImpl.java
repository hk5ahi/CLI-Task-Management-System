package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.SupervisorDao;
import server.dao.UserDao;
import server.domain.User;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final List<User> users=new ArrayList<>();

    private final SupervisorDao supervisorDao;
    private final ManagerDao managerDao;
    private final EmployeeDao employeeDao;

    public UserDaoImpl(SupervisorDao supervisorDao, ManagerDao managerDao, EmployeeDao employeeDao) {
        this.supervisorDao = supervisorDao;
        this.managerDao = managerDao;
        this.employeeDao = employeeDao;

    }

    @Override
    public void initializeUsers(){

        String supervisorName=supervisorDao.getSupervisors().get(0).getFirstName()+" " +supervisorDao.getSupervisors().get(0).getLastName();
        String managerName=managerDao.getManagers().get(0).getFirstName()+" "+managerDao.getManagers().get(0).getLastName();
        String employeeName=employeeDao.getEmployees().get(0).getFirstName()+" "+employeeDao.getEmployees().get(0).getLastName();
        users.add(supervisorDao.getSupervisorByName(supervisorName));
        users.add(employeeDao.getEmployeeByName(employeeName));
        users.add(managerDao.getManagerByName(managerName));

    }

   @Override
    public void addUser(User user)
    {
        users.add(user);

    }
    @Override
    public List<User> getAllUsers()
    {
        return users;

    }
    @Override
    public boolean userExist(String userName)
    {
        for(User user:users)
        {
            if(user.getUsername().equals(userName))
            {
                return true;

            }

        }
        return false;

    }
}
