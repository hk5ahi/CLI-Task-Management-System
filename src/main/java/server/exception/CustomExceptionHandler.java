package server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.exception.ForbiddenAccessException;
import server.exception.UserNotFoundException;


@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @ExceptionHandler(StatusOKMessage.class)
    public ResponseEntity<String> handleStatusOKMessage(StatusOKMessage ex) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ExceptionHandler(StatusCREATEDMessage.class)
    public ResponseEntity<String> handleStatusCREATEDMessage(StatusCREATEDMessage ex) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
