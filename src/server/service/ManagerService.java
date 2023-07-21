package server.service;

import server.domain.Manager;

public interface ManagerService {

    Manager findManager(String username, String password);

    void createManager(String firstname, String lastname, String username, String password);

    void viewAllManagers();


    void inReviewToCompleted(Manager activeManager);


    void viewAllTasksCreatedByManager(Manager activeManager);




}
