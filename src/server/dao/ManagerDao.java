package server.dao;

import server.domain.Manager;

import java.util.List;

public interface ManagerDao {
    List<Manager> getAllManagers();

    Manager getManagerByUsername(String username);

    Manager createManager(Manager manager);

    boolean updateManager(Manager manager);

    boolean deleteManager(String username);
}
