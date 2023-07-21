package server.domain;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User {

    private static List<Manager> managers = new ArrayList<>();

    public Manager(String firstname, String lastname, String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstname);
        this.setLastName(lastname);
        this.addManager(this);
    }

    public static List<Manager> getManagers() {
        return managers;
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

}
