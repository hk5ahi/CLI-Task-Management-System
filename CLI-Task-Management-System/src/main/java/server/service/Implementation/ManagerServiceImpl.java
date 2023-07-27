package server.service.Implementation;

import server.dao.implementation.ManagerDaoImpl;
import server.dao.implementation.TaskDaoImpl;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.service.ManagerService;

public class ManagerServiceImpl implements ManagerService {

    private ManagerDaoImpl managerDao = ManagerDaoImpl.getInstance();
    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();

    @Override
    public Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : managerDao.getManagers()) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager; // Return the matched Manager object
            }
        }
        return null; // If no match found, return null
    }


    @Override
    public void initializeManager(Manager manager) {
        manager.setFirstName("Muhammad");
        manager.setLastName("Ubaid");
        manager.setUsername("m.ubaid");
        manager.setPassword("Ts12");
        manager.setUserRole("Manager");
        managerDao.addManager(manager);
    }

    @Override
    public User getManagerByName(String name) {
        for (Manager manager : managerDao.getManagers()) {
            String managerName = manager.getFirstName() + " " + manager.getLastName();
            if (managerName.equalsIgnoreCase(name)) {
                Manager foundManager = new Manager();
                foundManager.setFirstName("Manager");
                return foundManager;
            }
        }
        return null;
    }

    @Override
    public void viewAllTasksCreatedByManager(Manager activeManager) {
        System.out.println("The tasks which are created by the Manager are: ");

        boolean foundTasks = false; // To track if any tasks are found for the manager

        for (Task task : taskDao.getAllTasksbyEmployee()) {
            String createdByFullName = "N/A"; // Default value in case createdBy is null
            String assigneeFullName = "N/A"; // Default value in case assignee is null

            if (task.getCreatedBy() != null) {
                createdByFullName = task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName();
            }

            if (task.getAssignee() != null) {
                assigneeFullName = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }

            String activeManagerFullName = activeManager.getFirstName() + " " + activeManager.getLastName();

            if (createdByFullName.equals(activeManagerFullName)) {
                String taskTitle = task.getTitle();
                String taskDescription = task.getDescription();

                System.out.printf("The title of the task is %s with its description which is %s and its employee is %s.\n", taskTitle, taskDescription, assigneeFullName);

                foundTasks = true; // Set the flag to true as tasks are found
            }
        }

        // If no tasks are found, print "None"
        if (!foundTasks) {
            System.out.println("None");
        }
    }


    @Override
    public void viewAllManagers() {
        System.out.println("The Managers of the System are:");

        for (Manager manager : managerDao.getManagers()) {
            System.out.println(manager.getFirstName() + " " + manager.getLastName());
        }
    }







}
