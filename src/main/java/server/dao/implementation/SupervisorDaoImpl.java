package server.dao.implementation;

import server.dao.SupervisorDao;
import server.dao.UserDao;
import server.domain.Supervisor;

public class SupervisorDaoImpl implements SupervisorDao {

    private static SupervisorDaoImpl instance; // Singleton instance
    private final Supervisor supervisor; // Instance variable to store supervisor information


    // Private constructor to prevent external instantiation
    private SupervisorDaoImpl() {
        supervisor = new Supervisor();
    }



    public static SupervisorDaoImpl getInstance() {
        if (instance == null) {
            instance = new SupervisorDaoImpl();
        }
        return instance;
    }


    @Override
    public void setSupervisorInfo(String firstName, String lastName, String username, String password, String userRole) {
        supervisor.setFirstName(firstName);
        supervisor.setLastName(lastName);
        supervisor.setUsername(username);
        supervisor.setPassword(password);
        supervisor.setUserRole(userRole);

    }

    @Override
    public Supervisor getSupervisorInfo() {
        return supervisor;
    }


}
