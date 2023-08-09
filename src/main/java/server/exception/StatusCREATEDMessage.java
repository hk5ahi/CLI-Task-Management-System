package server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CREATED)
public class StatusCREATEDMessage extends RuntimeException {
    // Add any additional constructors or messages if needed
}