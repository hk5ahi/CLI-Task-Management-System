import java.util.List;

public interface ManagerService {

    Manager findManager(String username, String password);

    boolean addManager(Manager manager);

    void deleteManager(String username);

    List<Manager> getAllManagers();

    void createTask(String title, String description, int total_time);

    void assignTask();

    void inReviewToCompleted();

    void addComments(String message);

    void viewAllTasks();

    void viewAllTasksByEmployee();

    void viewAllTasksByStatus();

    void viewAllTasksByEmployeeAndStatus();

}
