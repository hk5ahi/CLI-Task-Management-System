package server.service;
import server.domain.Supervisor;


public interface SupervisorService {

    void initializeSupervisor(Supervisor supervisor);

    Supervisor verifyCredentials(String username, String password);

    void viewSupervisor();


}

