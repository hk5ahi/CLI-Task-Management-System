package server.service.Implementation;

import server.domain.Manager;
import server.domain.Task;
import server.service.ManagerService;
import server.service.TaskService;

import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getAllTasks;
import static server.domain.Manager.getManagers;

public class ManagerServiceImpl implements ManagerService {

    TaskService taskService = new TaskServiceImpl();
    @Override
    public Manager findManager(String providedUsername, String providedPassword) {
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager; // Return the matched Employee object
            }
        }
        return null; // If no match found, return null
    }





    @Override
    public void inReviewToCompleted(Manager activeManager) {

        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task whose status you want to change.");
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        if (task.getTaskStatus().equals("IN_REVIEW")) {
            task.setTaskStatus(String.valueOf(Task.Status.COMPLETED));
            task.getHistory().setTimestamp(LocalDateTime.now().toString());
            task.getHistory().setOldStatus("IN_REVIEW");
            task.getHistory().setNewStatus("COMPLETED");
            task.getHistory().setMovedBy(activeManager.getFirstName() + " " + activeManager.getLastName());


        } else {

            System.out.println("The task is not in desirable state.");
        }


    }

    @Override
    public void viewAllTasksCreatedByManager(Manager activeManager) {
        System.out.println("The tasks which are created by Manager himself are: ");
        for (int i = 0; i < getAllTasks().size(); i++) {
            if (getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {
                System.out.printf("The title of task is %s with its description which is %s and its employee is %s.\n", getAllTasks().get(i).getTitle(), getAllTasks().get(i).getDescription(), getAllTasks().get(i).getAssignee());
            }

        }


    }

    @Override
    public void createManager(String firstname, String lastname, String username, String password) {

        Manager m = new Manager(firstname, lastname, username, password);
        System.out.println("Manager created successfully.");
    }

    @Override
    public void viewAllManagers() {

        System.out.println("The Managers of the System are:");

        for (int i = 0; i < getManagers().size(); i++) {
            System.out.println(getManagers().get(i).getFirstName() + " " + getManagers().get(i).getLastName());

        }

    }






}
