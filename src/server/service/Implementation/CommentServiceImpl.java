package server.service.Implementation;

import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.service.CommentService;

import java.time.LocalDateTime;
import java.util.Scanner;

import static server.domain.Employee.getAllTasks;

public class CommentServiceImpl implements CommentService {

    TaskServiceImpl taskService = new TaskServiceImpl();

    @Override
    public void addComments(String message, User person) {
        Scanner scan = new Scanner(System.in);
        Task task = null;
        System.out.println("The Tasks are:");
        for (Task t : getAllTasks()) {
            System.out.println(t.getTitle());
        }

        do {
            System.out.println("Enter the title of task that you want to comment.");
            String title = scan.nextLine();
            task = taskService.getTaskByTitle(title);
            if (task == null) {
                System.out.println("Wrong title entered");
            }
        } while (task == null);

        Comment c = new Comment();
        c.setBody(message);
        c.setCreatedAt(LocalDateTime.now().toString());
        c.setCreatedBy(person.getFirstName() + " " + person.getLastName());
        task.addComment(c);
        System.out.printf("The comment has been added successfully by %s.\n", person.getFirstName() + " " + person.getLastName());

    }


}
