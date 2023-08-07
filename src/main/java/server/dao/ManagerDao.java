package server.dao;

import server.domain.Manager;
import server.domain.User;

import java.util.List;

public interface ManagerDao {

    List<Manager> getManagers();
    Manager findManager(String username, String password);
    User getManagerByName(String name);
    void addManager(Manager manager);

    Manager createManager(String firstname, String lastname, String username, String password);


}
