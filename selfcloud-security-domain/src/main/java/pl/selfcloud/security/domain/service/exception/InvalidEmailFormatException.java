package pl.selfcloud.security.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEmailFormatException extends RuntimeException {
  public InvalidEmailFormatException(String email) {
    super("The email address " + email + " has an invalid format.");
  }
}