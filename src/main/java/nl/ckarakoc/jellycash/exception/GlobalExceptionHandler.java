package nl.ckarakoc.jellycash.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.ApiError;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Environment env;

  @ExceptionHandler(NotImplementedException.class)
  public ResponseEntity<String> handleNotImplementedException(NotImplementedException ex) {
    return ResponseEntity.internalServerError().body("Not implemented yet");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("error", "Internal Server Error");
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    if (env.acceptsProfiles(Profiles.of("dev"))
        || env.acceptsProfiles(Profiles.of("test"))) {
      body.put("message", ex.getMessage());
    }
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiError> handleApiException(ApiException ex) {
    ApiError apiError = new ApiError(HttpStatus.I_AM_A_TEAPOT.value(), "Api Error",
        ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.I_AM_A_TEAPOT);
  }

  @ExceptionHandler(CreationException.class)
  public ResponseEntity<ApiError> handleCreationException(CreationException ex) {
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Creation Error",
        ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<ApiError> handleMalformedJwtException(MalformedJwtException ex) {
    ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token",
        ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ApiError> handleExpiredJwtException(ExpiredJwtException ex) {
    ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), "JWT token is expired",
        ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UnsupportedJwtException.class)
  public ResponseEntity<ApiError> handleUnsupportedJwtException(UnsupportedJwtException ex) {
    ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), "JWT token is unsupported",
        ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, String> violations = ex.getConstraintViolations()
        .stream()
        .collect(Collectors.toMap(
            v -> v.getPropertyPath().toString(),
            v -> v.getMessage(),
            (msg1, msg2) -> msg1
        ));

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Constraint Violation",
        violations);

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));

    ex.getBindingResult().getGlobalErrors().stream()
        .filter(error -> Objects.equals(error.getCode(), "PasswordMatches"))
        .forEach(error -> errors.put("password", error.getDefaultMessage()));

    ApiError apiError = new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        "Validation Error",
        errors
    );

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }
}
