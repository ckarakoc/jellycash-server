package nl.ckarakoc.jellycash.exception;

public class AuthenticationForbiddenException extends RuntimeException {

  public AuthenticationForbiddenException(String message) {
    super(message);
  }
}
