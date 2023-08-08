package server.dao;

import server.domain.Manager;
import server.domain.User;

import java.util.List;
import java.util.Optional;

public interface ManagerDao {

    List<Manager> getManagers();
    Optional<Manager> findManager(String username, String password);
    Optional<Manager> getManagerByName(String name);
    void addManager(Manager manager);

    Manager createManager(String firstname, String lastname, String username, String password);


}
