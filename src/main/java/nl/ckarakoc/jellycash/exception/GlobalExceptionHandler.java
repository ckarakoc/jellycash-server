package nl.ckarakoc.jellycash.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Environment env;

	public GlobalExceptionHandler(Environment env) {
		this.env = env;
	}

	@ExceptionHandler(NotImplementedException.class)
	public void handleNotImplementedException(NotImplementedException ex) {

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("error", "Internal Server Error");
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		if (isDev) body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Constraint Violation");

		Map<String, String> violations = ex.getConstraintViolations()
			.stream()
			.collect(Collectors.toMap(
				v -> v.getPropertyPath().toString(),
				v -> v.getMessage(),
				(msg1, msg2) -> msg1
			));

		body.put("errors", violations);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
