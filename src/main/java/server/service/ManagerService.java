package server.service;

import server.domain.Manager;
import server.domain.User;

public interface ManagerService {

    void viewAllManagers();

    void viewAllTasksCreatedByManager(Manager activeManager);




}
