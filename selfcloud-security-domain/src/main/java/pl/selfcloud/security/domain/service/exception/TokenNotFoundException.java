package pl.selfcloud.security.domain.service.exception;

public class TokenNotFoundException extends RuntimeException{

  public TokenNotFoundException(String value){
    super("Token with id " + value + " not found.");
  }

}
