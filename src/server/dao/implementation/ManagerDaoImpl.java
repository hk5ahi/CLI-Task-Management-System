package server.dao.implementation;

import server.dao.ManagerDao;
import server.domain.Manager;

import java.util.List;


public class ManagerDaoImpl implements ManagerDao {
    @Override
    public List<Manager> getManagers() {
        return Manager.getManagers();
    }

    @Override
    public void addManager(Manager manager) {
        Manager.getManagers().add(manager);
    }

    @Override
    public void createManager(String firstname, String lastname, String username, String password) {

        Manager m = new Manager(firstname, lastname, username, password);
        System.out.println("Manager created successfully.");
        addManager(m);
    }

}
