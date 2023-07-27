package server.service;

import server.domain.Manager;
import server.domain.User;

public interface ManagerService {

    Manager findManager(String username, String password);

    void initializeManager(Manager manager);

    void viewAllManagers();

    User getManagerByName(String name);

    void viewAllTasksCreatedByManager(Manager activeManager);




}
