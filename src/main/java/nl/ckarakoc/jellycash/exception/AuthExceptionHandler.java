package nl.ckarakoc.jellycash.exception;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleGenericAuthException(AuthenticationException ex) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		ApiError apiError = new ApiError(status.value(), "Authentication Error", ex.getMessage());
		return new ResponseEntity<>(apiError, status);
	}

	@ExceptionHandler(AuthenticationConflictException.class)
	public ResponseEntity<ApiError> handleAuthConflictException(AuthenticationConflictException ex) {
		HttpStatus status = HttpStatus.CONFLICT;
		ApiError apiError = new ApiError(status.value(), "Authentication Error", ex.getMessage());
		return new ResponseEntity<>(apiError, status);
	}
}
