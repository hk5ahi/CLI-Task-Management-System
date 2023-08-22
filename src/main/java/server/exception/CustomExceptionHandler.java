package server.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<String> handleForbiddenAccessException(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> handleUnAuthorizationException(UnAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
