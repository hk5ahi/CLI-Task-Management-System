package server.dao.implementation;

import server.dao.ManagerDao;
import server.domain.Manager;

import java.util.ArrayList;
import java.util.List;

public class ManagerDaoImpl implements ManagerDao {

    private static ManagerDaoImpl instance; // Singleton instance

    private UserDaoImpl userDao=UserDaoImpl.getInstance();
    private List<Manager> managers = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private ManagerDaoImpl() {
    }

    // Method to get the Singleton instance
    public static ManagerDaoImpl getInstance() {
        if (instance == null) {
            instance = new ManagerDaoImpl();
        }
        return instance;
    }

    @Override
    public List<Manager> getManagers() {
        return managers;
    }

    @Override
    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public void createManager(String firstName, String lastName, String username, String password) {
        Manager manager = new Manager(firstName, lastName, username, password);
        manager.setUserRole("Manager");
        addManager(manager);
        userDao.addUser(manager);
    }

}
