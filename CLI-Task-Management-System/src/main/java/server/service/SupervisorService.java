package server.service;

import server.domain.Supervisor;

public interface SupervisorService {

    // Initialize supervisor
    void initializeSupervisor();

    // Verify supervisor credentials
    Supervisor verifyCredentials(String username, String password);

    // View supervisor details
    void viewSupervisor();
}
