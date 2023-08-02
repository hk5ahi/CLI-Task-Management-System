package server.service.Implementation;

import org.springframework.stereotype.Service;
import server.dao.CommentDao;
import server.domain.Comment;
import server.domain.Task;
import server.domain.User;
import server.dto.ViewCommentDTO;
import server.service.CommentService;
import server.service.TaskService;

import java.time.LocalDateTime;
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
    public String addComments(String message, User person, String tasktitle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment();
        Task task=taskService.getTaskByTitle(tasktitle);
        comment.setBody(message);
        String formattedDateTime = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
        comment.setCreatedAt(dateTime);
        comment.setCreatedBy(person);
        comment.addTaskForComment(task);

        commentDao.addComment(comment);
       return "The comment has been added successfully by " +person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public List<ViewCommentDTO> viewComments(String title) {
        Task task = taskService.getTaskByTitle(title);
        List<Comment> comments = commentDao.getComments(task);
        List<ViewCommentDTO> viewCommentDTOList = new ArrayList<>();

        for (Comment comment : comments) {
            ViewCommentDTO commentDTO = new ViewCommentDTO();
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTO.setCreatedBy(comment.getCreatedBy().getFirstName() + " " + comment.getCreatedBy().getLastName());
            commentDTO.setMessage(comment.getBody());
            viewCommentDTOList.add(commentDTO);
        }

        return viewCommentDTOList;
    }




}
