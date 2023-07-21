package server.service;
import server.domain.Supervisor;


public interface SupervisorService {

    Supervisor verifyCredentials(String username, String password);

    void viewSupervisor();


}

