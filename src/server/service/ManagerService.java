package server.service;

import server.domain.Manager;

public interface ManagerService {

    Manager findManager(String username, String password);

    void initializeManager(Manager manager);

    void viewAllManagers();


    void inReviewToCompleted(Manager activeManager);


    void viewAllTasksCreatedByManager(Manager activeManager);




}
