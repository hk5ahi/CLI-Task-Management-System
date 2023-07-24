package server.service.Implementation;

import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.domain.Manager;
import server.domain.Task;
import server.service.ManagerService;
import server.service.TaskService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ManagerServiceImpl implements ManagerService {

    TaskService taskService = new TaskServiceImpl();
    @Override
    public Manager findManager(String providedUsername, String providedPassword) {
        ManagerDao managerDao = new ManagerDaoImpl();
        for (Manager manager : managerDao.getManagers()) {
            if (manager.getUsername().equals(providedUsername) && manager.getPassword().equals(providedPassword)) {
                return manager; // Return the matched Employee object
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
        ManagerDao managerDao = new ManagerDaoImpl();
        managerDao.addManager(manager);

    }


    @Override
    public void inReviewToCompleted(Manager activeManager) {

        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (Task t : employeeDao.getAllTasks()) {
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
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        System.out.println("The tasks which are created by Manager himself are: ");
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {
            if (employeeDao.getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {
                System.out.printf("The title of task is %s with its description which is %s and its employee is %s.\n", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription(), employeeDao.getAllTasks().get(i).getAssignee());
            }

        }


    }

    @Override
    public void viewAllManagers() {

        ManagerDao managerDao = new ManagerDaoImpl();
        System.out.println("The Managers of the System are:");

        for (int i = 0; i < managerDao.getManagers().size(); i++) {
            System.out.println(managerDao.getManagers().get(i).getFirstName() + " " + managerDao.getManagers().get(i).getLastName());

        }

    }






}
