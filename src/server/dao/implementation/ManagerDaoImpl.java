package server.dao;

import server.domain.Manager;

import java.util.ArrayList;
import java.util.List;

public class ManagerDaoImpl implements ManagerDao {

    private List<Manager> managers = new ArrayList<>();

    @Override
    public List<Manager> getAllManagers() {
        return managers;
    }

    @Override
    public Manager getManagerByUsername(String username) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username)) {
                return manager;
            }
        }
        return null;
    }

    @Override
    public Manager createManager(Manager manager) {
        managers.add(manager);
        return manager;
    }

    @Override
    public boolean updateManager(Manager manager) {
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getUsername().equals(manager.getUsername())) {
                managers.set(i, manager);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteManager(String username) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(username)) {
                managers.remove(manager);
                return true;
            }
        }
        return false;
    }
}
