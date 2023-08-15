package server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Manager;


import java.util.List;
import java.util.Optional;

public interface ManagerDao extends JpaRepository<Manager,String> {

    Optional<Manager> findManagerByUsernameAndPassword(String Username,String Password);

//    List<Manager> getManagers();
//    Optional<Manager> findManager(String username, String password);
//    Optional<Manager> getManagerByName(String name);
//    void addManager(Manager manager);

//    Manager createManager(String firstname, String lastname, String username, String password);


}
