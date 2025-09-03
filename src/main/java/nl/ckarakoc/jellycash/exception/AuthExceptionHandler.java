package nl.ckarakoc.jellycash.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
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

  @ExceptionHandler(AuthenticationRefreshTokenException.class)
  public ResponseEntity<ApiError> handleAuthRefreshTokenException(AuthenticationConflictException ex, HttpServletResponse response) {
    ResponseCookie clearAccess = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN, "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .sameSite("Strict")
        .build();

    ResponseCookie clearRefresh = ResponseCookie.from(AppConstants.JwtCookieNames.REFRESH_TOKEN, "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .sameSite("Strict")
        .build();

    response.addHeader("Set-Cookie", clearAccess.toString());
    response.addHeader("Set-Cookie", clearRefresh.toString());

    return handleGenericAuthenticationException(HttpStatus.UNAUTHORIZED, ex);
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

  @ExceptionHandler(CompromisedPasswordException.class)
  public ResponseEntity<ApiError> handleCompromisedPasswordException(CompromisedPasswordException ex) {
    return handleGenericAuthenticationException(HttpStatus.BAD_REQUEST, ex, "Compromised Password Detection");
  }

  private ResponseEntity<ApiError> handleGenericAuthenticationException(HttpStatus status, Exception ex) {
    return handleGenericAuthenticationException(status, ex, "Authentication Error");
  }

  private ResponseEntity<ApiError> handleGenericAuthenticationException(HttpStatus status, Exception ex, String error) {
    ApiError apiError = new ApiError(status.value(), error, ex.getMessage());
    return new ResponseEntity<>(apiError, status);
  }
}
