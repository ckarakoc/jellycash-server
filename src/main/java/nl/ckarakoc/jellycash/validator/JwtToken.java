package nl.ckarakoc.jellycash.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nl.ckarakoc.jellycash.validator.impl.JwtTokenValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {JwtTokenValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface JwtToken {
	String message() default "Invalid Jwt Token";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
