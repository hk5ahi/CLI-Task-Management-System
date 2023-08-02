package server.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden access")
public class ForbiddenAccessException extends RuntimeException {
    // Add any additional constructors or messages if needed
}
