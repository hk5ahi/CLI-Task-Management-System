package server.service.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.dao.CommentDao;
import server.dao.TaskDao;
import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.dto.CommentDTO;
import server.service.CommentService;
import server.service.TaskService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final TaskDao taskDao;

    public CommentServiceImpl(CommentDao commentDao, TaskDao taskDao) {
        this.commentDao = commentDao;
        this.taskDao = taskDao;

    }


    @Override
    public ResponseEntity<String> addComments(String message, User person, String tasktitle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment();

        Optional<Task> optionalTask = taskDao.getTaskByTitle(tasktitle);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Task task = optionalTask.get();
        String userName = person.getUsername();
        String assignee = task.getAssignee() != null ? task.getAssignee().getUsername() : "N/A";

        if (!person.getUserRole().equals(User.UserRole.Supervisor.toString())) {
            if (person.getUserRole().equals(User.UserRole.Manager.toString())) {
                if (!task.getCreatedBy().getUsername().equals(userName)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            } else if (person.getUserRole().equals(User.UserRole.Employee.toString())) {
                if (task.getAssignee() != null && !assignee.equals(userName)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
        }

        comment.setBody(message);
        String formattedDateTime = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
        comment.setCreatedAt(dateTime);
        comment.setCreatedBy(person);
        comment.addTaskForComment(task);

        commentDao.addComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Override
    public List<CommentDTO> viewComments(String title) {
        Optional<Task> optionalTask=taskDao.getTaskByTitle(title);
        if(optionalTask.isPresent()) {
            Task task=optionalTask.get();
            List<Comment> comments = commentDao.getComments(task);
            List<CommentDTO> viewCommentDTOList = new ArrayList<>();

            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();

                LocalDateTime timestamp = comment.getCreatedAt();
                LocalDateTime localDateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);
                commentDTO.setCreatedAt(formattedDateTime);
                commentDTO.setCreatedBy(comment.getCreatedBy().getFirstName() + " " + comment.getCreatedBy().getLastName());
                commentDTO.setMessage(comment.getBody());
                viewCommentDTOList.add(commentDTO);
            }

            return viewCommentDTOList;
        }
        else {
            return null;
        }
    }


}
