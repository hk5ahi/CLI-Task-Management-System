package server.service.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.dao.CommentDao;
import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.dto.CommentDTO;
import server.service.CommentService;
import server.service.TaskService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final TaskService taskService;

    public CommentServiceImpl(CommentDao commentDao, TaskService taskService) {
        this.commentDao = commentDao;
        this.taskService = taskService;
    }


    @Override
    public ResponseEntity<String> addComments(String message, User person, String tasktitle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment();
        Task task=taskService.getTaskByTitle(tasktitle);
        if(task!=null) {
            String fullname = person.getFirstName() + " " + person.getLastName();
            String assignee="";
            if(task.getAssignee()!=null) {
                 assignee = task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName();
            }
            if (!(person.getUserRole().equals(User.UserRole.Supervisor.toString()))) {

                if(person.getUserRole().equals(User.UserRole.Manager.toString()))
                {
                    if (!(task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName()).equals(fullname)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("her");
                }
                } else if (person.getUserRole().equals(User.UserRole.Employee.toString())) {
                    if (task.getAssignee() != null && !assignee.equals(fullname)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("his");
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
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public List<CommentDTO> viewComments(String title) {
        Task task = taskService.getTaskByTitle(title);
        if (task != null) {
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
