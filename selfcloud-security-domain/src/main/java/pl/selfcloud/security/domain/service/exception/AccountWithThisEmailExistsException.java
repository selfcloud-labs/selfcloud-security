package pl.selfcloud.security.domain.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class AccountWithThisEmailExistsException extends RuntimeException{
  public AccountWithThisEmailExistsException(String email) {
    super("The account with email " + email + " already exist.");
  }
}
