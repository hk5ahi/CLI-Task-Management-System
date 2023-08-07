package server.service;

import server.domain.Supervisor;

public interface SupervisorService {




    Supervisor getAndVerify(String username, String password);

    // View supervisor details
    void viewSupervisor();
}
