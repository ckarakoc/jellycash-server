package nl.ckarakoc.jellycash.exception;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class AuthExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleGenericAuthException(AuthenticationException ex) {
    return handleGenericAuthenticationException(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler(AuthenticationConflictException.class)
  public ResponseEntity<ApiError> handleAuthConflictException(AuthenticationConflictException ex) {
    return handleGenericAuthenticationException(HttpStatus.CONFLICT, ex);
  }

  @ExceptionHandler(AuthenticationForbiddenException.class)
  public ResponseEntity<ApiError> handleAuthForbiddenException(AuthenticationConflictException ex) {
    return handleGenericAuthenticationException(HttpStatus.FORBIDDEN, ex);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ApiError> handleDisabledException(DisabledException ex) {
    return handleGenericAuthenticationException(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ApiError> handleLockedException(LockedException ex) {
    return handleGenericAuthenticationException(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {
    return handleGenericAuthenticationException(HttpStatus.UNAUTHORIZED, ex);
  }

  private ResponseEntity<ApiError> handleGenericAuthenticationException(HttpStatus status, Exception ex) {
    ApiError apiError = new ApiError(status.value(), "Authentication Error", ex.getMessage());
    return new ResponseEntity<>(apiError, status);
  }
}
