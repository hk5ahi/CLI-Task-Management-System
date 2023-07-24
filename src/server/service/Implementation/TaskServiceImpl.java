package server.service.Implementation;

import server.dao.EmployeeDao;
import server.dao.ManagerDao;
import server.dao.implementation.EmployeeDaoImpl;
import server.dao.implementation.ManagerDaoImpl;
import server.domain.Employee;
import server.domain.Manager;
import server.domain.Task;
import server.service.EmployeeService;
import server.service.TaskService;

import java.util.Scanner;

public class TaskServiceImpl implements TaskService {

    EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    public void viewAllTasks() {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.It is created by %s", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription(), employeeDao.getAllTasks().get(i).getAssignee(), employeeDao.getAllTasks().get(i).getTaskStatus(), employeeDao.getAllTasks().get(i).getCreatedBy());


        }

    }

    @Override
    public void viewAllTasksByStatusCreatedBySingleManager(Manager activeManager) {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {
            if (employeeDao.getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s and its status is %s.\n", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription(), employeeDao.getAllTasks().get(i).getTaskStatus());
            }

        }
    }

    @Override
    public void viewAllTasksByEmployeeAndStatusCreatedBySingleManager(Manager activeManager) {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {
            if (employeeDao.getAllTasks().get(i).getCreatedBy().equals(activeManager.getFirstName() + " " + activeManager.getLastName())) {

                System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.\n", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription(), employeeDao.getAllTasks().get(i).getAssignee(), employeeDao.getAllTasks().get(i).getTaskStatus());
            }

        }
    }

    @Override
    public void viewTasksByStatus() {
        EmployeeDao employeeDao = new EmployeeDaoImpl();

        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are CREATED are:");
            if (employeeDao.getAllTasks().get(i).getTaskStatus().equals("CREATED")) {

                System.out.printf("The title of task is %s with its description which is %s.", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");

        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are In Progress are:");
            if (employeeDao.getAllTasks().get(i).getTaskStatus().equals("IN_PROGRESS")) {

                System.out.printf("The title of task is %s with its description which is %s.", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are In Review are:");
            if (employeeDao.getAllTasks().get(i).getTaskStatus().equals("IN_REVIEW")) {

                System.out.printf("The title of task is %s with its description which is %s.", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");
        for (int i = 0; i < employeeDao.getAllTasks().size(); i++) {

            System.out.println("The tasks whose status are COMPLETED are:");
            if (employeeDao.getAllTasks().get(i).getTaskStatus().equals("COMPLETED")) {

                System.out.printf("The title of task is %s with its description which is %s.", employeeDao.getAllTasks().get(i).getTitle(), employeeDao.getAllTasks().get(i).getDescription());
            }

        }
        System.out.println(" ");

    }

    @Override
    public void assignTask() {
        String title1;
        String name11 = null;
        Task task = null;
        Scanner scan = new Scanner(System.in);
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        System.out.println("The Tasks are:");
        for (Task t : employeeDao.getAllTasks()) {
            System.out.println(t.getTitle());
        }

        // Loop until the correct title is entered
        do {
            System.out.println("Enter the title of the task that you want to assign.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        title1 = task.getTitle();

        System.out.println("The Employees are:");
        for (Employee employee : employeeDao.getEmployees()) {
            System.out.println(employee.getFirstName() + " " + employee.getLastName());
        }

        // Loop until the correct name is entered and it's not the manager's name
        do {
            System.out.println("Enter the full name of the employee that you want to assign.");
            String name = scan.nextLine();
            name11 = employeeService.getEmployeeByName(name);

            // Check if the selected employee is the manager
            if (name11.equals(" ")) {
                // If the name is valid and not the manager's name, proceed with task assignment
                System.out.println("The task can not be assigned to manager.");

            } else if (name11 != null && !name11.equals(" ")) {
                // If the name is the manager's name, display a message and re-loop to get a different name
                break;
            } else {
                // If the name is not found, display a message and re-loop to get a different name
                System.out.println("Wrong name entered");
            }
        } while (true);

        if (task != null && name11 != null) {

            if (task.isAssigned()) {
                System.out.println("Already assigned task.");
            } else {
                task.setAssignee(name11);
                task.setAssigned(true);
                System.out.printf("The Task titled %s is assigned to %s.\n", title1, name11);
                // Add the task to the assigned tasks list of the selected employee
                for (Employee employee : employeeDao.getEmployees()) {
                    String name = employee.getFirstName() + " " + employee.getLastName();
                    if (name.equalsIgnoreCase(name11)) {
                        employee.setAssignedTasks(task, employee);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void viewTasksByEmployee() {
        System.out.println("The tasks are categorized employee-wise with their respective statuses.");
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (int i = 0; i < employeeDao.getEmployees().size(); i++) {

            System.out.printf("The name of Employee is %s %s and its assigned tasks with their status are:\n", employeeDao.getEmployees().get(i).getFirstName(), employeeDao.getEmployees().get(i).getLastName());

            for (int j = 0; j < employeeDao.getAllTasks().size(); j++) {
                if (employeeDao.getAllTasks().get(j).getAssignee().equals(employeeDao.getEmployees().get(i).getFirstName() + " " + employeeDao.getEmployees().get(i).getLastName())) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s and is created by %s.", employeeDao.getAllTasks().get(j).getTitle(), employeeDao.getAllTasks().get(j).getDescription(), employeeDao.getAllTasks().get(j).getTaskStatus(), employeeDao.getAllTasks().get(i).getCreatedBy());

                }

            }

        }


    }

    @Override
    public void viewTasksByManager() {
        ManagerDao managerDao = new ManagerDaoImpl();
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        System.out.println("The tasks are categorized manager-wise with their respective statuses.");

        for (int i = 0; i < managerDao.getManagers().size(); i++) {

            System.out.printf("The name of Manager is %s %s and its created tasks with their status are:\n", managerDao.getManagers().get(i).getFirstName(), managerDao.getManagers().get(i).getLastName());

            for (int j = 0; j < employeeDao.getAllTasks().size(); j++) {
                if (employeeDao.getAllTasks().get(j).getCreatedBy().equals(managerDao.getManagers().get(i).getFirstName() + " " + managerDao.getManagers().get(i).getLastName())) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", employeeDao.getAllTasks().get(j).getTitle(), employeeDao.getAllTasks().get(j).getDescription(), employeeDao.getAllTasks().get(j).getTaskStatus());

                }

            }

        }


    }

    @Override
    public void viewAssignedTasks(Employee employee) {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        if (employeeDao.getAssignedTasks(employee).size() != 0) {


            System.out.print("The assigned tasks for the employee are:\n");
            for (int i = 0; i < employeeDao.getAssignedTasks(employee).size(); i++) {
                System.out.print(employeeDao.getAssignedTasks(employee).get(i).getDescription());
            }
        } else {

            System.out.println("No Tasks.");
        }
    }

    @Override
    public Task getTaskByTitle(String title) {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        for (Task task : employeeDao.getAllTasks()) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null; // Task not found

    }



    @Override

    public void archiveTask() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        System.out.println("The Tasks are:");
        for (Task t : employeeDao.getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task that you want to archive.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        task.setAssigned(false);
        task.setAssignee(null);
        //status of task !!!!
        //task.taskStatus= String.valueOf(server.domain.Task.Status.CREATED);

        System.out.println("The task has been archive successfully.");

    }


}
