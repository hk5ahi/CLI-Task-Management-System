package server.dao;

import server.domain.Manager;

import java.util.List;

public interface ManagerDao {

    List<Manager> getManagers();

    void addManager(Manager manager);

    void createManager(String firstname, String lastname, String username, String password);


}
