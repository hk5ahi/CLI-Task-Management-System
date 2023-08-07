package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Manager;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ManagerDaoImpl implements ManagerDao {

    private List<Manager> managers = new ArrayList<>(List.of(new Manager("Muhammad", "Ubaid", "m.ubaid", "Ts12", "Manager")));


    @Override
    public List<Manager> getManagers() {
        return managers;
    }

    @Override
    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public Manager createManager(String firstName, String lastName, String username, String password) {
        Manager manager = new Manager(firstName, lastName, username, password,"Manager");
        managers.add(manager);
        return manager;
    }

    @Override
    public Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager; // Return the matched Manager object
            }
        }
        return null; // If no match found, return null
    }
    @Override
    public User getManagerByName(String name) {
        for (Manager manager : managers) {
            String managerName = manager.getFirstName() + " " + manager.getLastName();
            if (managerName.equalsIgnoreCase(name)) {

                return manager;
            }
        }
        return null;
    }

}
