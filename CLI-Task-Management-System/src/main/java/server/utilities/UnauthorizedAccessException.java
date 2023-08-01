package server.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized access")
public class UnauthorizedAccessException extends RuntimeException {
    // Add any additional constructors or messages if needed
}
