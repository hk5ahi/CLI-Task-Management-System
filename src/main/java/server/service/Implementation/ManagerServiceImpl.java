package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.ManagerDao;
import server.dao.TaskDao;
import server.dao.UserDao;
import server.domain.Manager;
import server.domain.Task;
import server.domain.User;
import server.service.ManagerService;
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerDao managerDao;
    private final TaskDao taskDao;


    public ManagerServiceImpl(ManagerDao managerDao, TaskDao taskDao) {
        this.managerDao = managerDao;
        this.taskDao = taskDao;

    }



    @Override
    public void viewAllTasksCreatedByManager(Manager activeManager) {
        System.out.println("The tasks which are created by the Manager are: ");

        boolean foundTasks = false; // To track if any tasks are found for the manager

        for (Task task : taskDao.getAll()) {
            String createdByFullName ="N/A";
            String assigneeFullName = "N/A";

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
