//package server.dao.implementation;
//
//import org.springframework.stereotype.Repository;
//import server.dao.EmployeeDao;
//import server.dao.ManagerDao;
//import server.dao.SupervisorDao;
//import server.dao.UserDao;
//import server.domain.Employee;
//import server.domain.Manager;
//import server.domain.Supervisor;
//import server.domain.User;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class UserDaoImpl implements UserDao {
//
//    private final List<User> users=new ArrayList<>();
//
//    private final SupervisorDao supervisorDao;
//    private final ManagerDao managerDao;
//    private final EmployeeDao employeeDao;
//
//    public UserDaoImpl(SupervisorDao supervisorDao, ManagerDao managerDao, EmployeeDao employeeDao) {
//        this.supervisorDao = supervisorDao;
//        this.managerDao = managerDao;
//        this.employeeDao = employeeDao;
//
//    }
//
//    @Override
//    public void initializeUsers() {
//        Supervisor supervisor = supervisorDao.getSupervisors().get(0);
//        Manager manager = managerDao.getManagers().get(0);
//        Employee employee = employeeDao.getEmployees().get(0);
//
//        supervisorDao.getSupervisorByName(supervisor.getFirstName() + " " + supervisor.getLastName())
//                .ifPresent(users::add);
//
//        employeeDao.getEmployeeByName(employee.getFirstName() + " " + employee.getLastName())
//                .ifPresent(users::add);
//
//        managerDao.getManagerByName(manager.getFirstName() + " " + manager.getLastName())
//                .ifPresent(users::add);
//    }
//
//
//    @Override
//    public Optional<User> getByUsername(String username) {
//        return users.stream()
//                .filter(user -> user.getUsername().equals(username))
//                .findFirst();
//    }
//
//    @Override
//    public void addUser(User user)
//    {
//        users.add(user);
//
//    }
//    @Override
//    public List<User> getAllUsers()
//    {
//        return users;
//
//    }
//    @Override
//    public boolean userExist(String userName) {
//        return users.stream().anyMatch(user -> user.getUsername().equals(userName));
//    }
//
//}
