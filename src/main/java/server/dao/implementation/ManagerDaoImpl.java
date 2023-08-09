package server.dao.implementation;

import org.springframework.stereotype.Repository;
import server.dao.ManagerDao;
import server.dao.UserDao;
import server.domain.Manager;
import server.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ManagerDaoImpl implements ManagerDao {

    private List<Manager> managers = new ArrayList<>(List.of(new Manager("Muhammad", "Ubaid", "m.ubaid", "Ts12", User.UserRole.Manager)));


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
        Manager manager = new Manager(firstName, lastName, username, password, User.UserRole.Manager);
        managers.add(manager);
        return manager;
    }

    @Override
    public Optional<Manager> findManager(String providedUsername, String providedPassword) {
        for (Manager manager : managers) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return Optional.of(manager); // Return the matched Manager object
            }
        }
        return Optional.empty(); // If no match found, return null
    }
    @Override
    public Optional<Manager> getManagerByName(String name) {
        for (Manager manager : managers) {
            String managerName = manager.getFirstName() + " " + manager.getLastName();
            if (managerName.equalsIgnoreCase(name)) {

                return Optional.of(manager);
            }
        }
        return Optional.empty();
    }

}
