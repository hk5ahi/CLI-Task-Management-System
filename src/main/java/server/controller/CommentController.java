package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.dto.CommentDTO;
import server.service.CommentService;
import java.util.List;
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<String> create(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentDTO comment
    ) {

        commentService.addCommentByUser(comment,authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping()
    public ResponseEntity <List<CommentDTO>> getComments(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("title") String title
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(title,authorizationHeader));

}
}