package nl.ckarakoc.jellycash.exception;

public class AuthenticationConflictException extends RuntimeException {
	public AuthenticationConflictException(String message) {
		super(message);
	}
}
