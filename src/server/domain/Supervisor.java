package server.domain;

public class Supervisor extends User {
    private static Supervisor instance;

    public Supervisor() {
        setUsername("m.asif");
        setPassword("Ts12");
        setUserRole("server.domain.Supervisor");
    }

    public static Supervisor getInstance() {
        if (instance == null) {
            // Create the instance if it's not already created
            instance = new Supervisor();
        }
        return instance;
    }

    // Method to verify credentials and return the server.domain.Supervisor object if matched
    public static Supervisor verifyCredentials(String providedUsername, String providedPassword) {
        if (getInstance().getUsername().equals(providedUsername) && getInstance().getPassword().equals(providedPassword)) {
            return getInstance(); // Return the current server.domain.Supervisor object
        } else {
            return null; // If the credentials don't match, return null
        }
    }

    public static void createManager(String firstname, String lastname, String username, String password) {

        Manager m = new Manager(firstname, lastname, username, password);
        System.out.println("server.domain.Manager created successfully.");
    }

    void viewallTasks() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.printf("The title of task is %s with its description which is %s whose employee is %s and  its status is %s.It is created by %s", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description, Employee.allTasks.get(i).assignee, Employee.allTasks.get(i).taskStatus, Employee.allTasks.get(i).createdBy);


        }

    }

    void viewbyStatus() {

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are CREATED are:");
            if (Employee.allTasks.get(i).taskStatus == "CREATED") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");

        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are In Progress are:");
            if (Employee.allTasks.get(i).taskStatus == "IN_PROGRESS") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are In Review are:");
            if (Employee.allTasks.get(i).taskStatus == "IN_REVIEW") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");
        for (int i = 0; i < Employee.allTasks.size(); i++) {

            System.out.println("The tasks whose status are COMPLETED are:");
            if (Employee.allTasks.get(i).taskStatus == "COMPLETED") {

                System.out.printf("The title of task is %s with its description which is %s.", Employee.allTasks.get(i).title, Employee.allTasks.get(i).description);
            }

        }
        System.out.println(" ");

    }

    void viewbyEmp() {
        System.out.println("The tasks are categorized employee-wise with their respective statuses.");

        for (int i = 0; i < Employee.employees.size(); i++) {

            System.out.printf("The name of server.domain.Employee is %s %s and its assigned tasks with their status are:\n", Employee.employees.get(i).first_Name, Employee.employees.get(i).last_Name);

            for (int j = 0; j < Employee.allTasks.size(); j++) {
                if (Employee.allTasks.get(j).assignee.equals(Employee.employees.get(i).first_Name + " " + Employee.employees.get(i).last_Name)) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", Employee.allTasks.get(j).title, Employee.allTasks.get(j).description, Employee.allTasks.get(j).taskStatus);

                }

            }

        }


    }

    void viewbyManager() {
        System.out.println("The tasks are categorized manager-wise with their respective statuses.");

        for (int i = 0; i < Manager.managers.size(); i++) {

            System.out.printf("The name of server.domain.Manager is %s %s and its created tasks with their status are:\n", Manager.managers.get(i).first_Name, Manager.managers.get(i).last_Name);

            for (int j = 0; j < Employee.allTasks.size(); j++) {
                if (Employee.allTasks.get(j).createdBy.equals(Manager.managers.get(i).first_Name + " " + Manager.managers.get(i).last_Name)) {

                    System.out.printf("The title of task is %s with its description which is %s and its status is %s.", Employee.allTasks.get(j).title, Employee.allTasks.get(j).description, Employee.allTasks.get(j).taskStatus);

                }

            }

        }


    }

    void archiveTask() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task that you want to archive.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        task.assigned = false;
        task.assignee = null;
        //status of task !!!!
        //task.taskStatus= String.valueOf(server.domain.Task.Status.CREATED);

        System.out.println("The task has been archive successfully.");
    }

    void addComments(String message) {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task that you want to comment.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        Comment c = new Comment();
        c.body = message;
        c.createdAt = (LocalDateTime.now().toString());
        c.createdBy = first_Name + " " + last_Name;
        task.comments.add(c);

    }

    void viewTaskHistory() {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : Employee.allTasks) {
            System.out.println(t.title);
        }

        do {
            System.out.println("Enter the title of task whose task history you want to see.");
            String title = scan.nextLine();
            task = getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        System.out.printf("The old status of task is %s , its new status is %s, the movement occurred at date and time which is %s and its moved by %s", task.history.old_status, task.history.new_status, task.history.timestamp.toString(), task.history.moved_by);
    }

    void viewallEmp() {
        System.out.println("The server.domain.Supervisor of the System is:");

        System.out.println(instance.first_Name + " " + instance.last_Name);


        System.out.println("The Managers of the System are:");

        for (int i = 0; i < Manager.managers.size(); i++) {
            System.out.println(Manager.managers.get(i).first_Name + " " + Manager.managers.get(i).last_Name);

        }
        System.out.println("The Employees of the System are:");

        for (int i = 0; i < Employee.employees.size(); i++) {
            System.out.println(Employee.employees.get(i).first_Name + " " + Employee.employees.get(i).last_Name);

        }
    }

    public static void createEmployee(String firstname, String lastname, String username, String password) {

        Employee e = new Employee(firstname, lastname, username, password);
        System.out.println("server.domain.Employee created successfully.");

    }

    // You can add other getters and setters for remaining properties if needed
}
