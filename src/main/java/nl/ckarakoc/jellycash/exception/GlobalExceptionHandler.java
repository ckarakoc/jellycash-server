package nl.ckarakoc.jellycash.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Environment env;

	public GlobalExceptionHandler(Environment env) {
		this.env = env;
	}

	@ExceptionHandler(NotImplementedException.class)
	public String handleNotImplementedException(NotImplementedException ex) {
		return "Not implemented yet";
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("error", "Internal Server Error");
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		if (env.acceptsProfiles(Profiles.of("dev")) ||
			env.acceptsProfiles(Profiles.of("test")))
			body.put("message", ex.getMessage());

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

		if (env.acceptsProfiles(Profiles.of("dev")) ||
			env.acceptsProfiles(Profiles.of("test")))
			body.put("errors", violations);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage()));

		ex.getBindingResult().getGlobalErrors().stream()
			.filter(error -> Objects.equals(error.getCode(), "PasswordMatches"))
			.forEach(error -> errors.put("password", error.getDefaultMessage()));

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation Error");
		if (env.acceptsProfiles(Profiles.of("dev")) ||
			env.acceptsProfiles(Profiles.of("test")))
			body.put("errors", errors);
		return ResponseEntity.badRequest().body(body);
	}
}
