package nl.ckarakoc.jellycash.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import nl.ckarakoc.jellycash.validator.impl.ValidTransactionValidator;

@Documented
@Constraint(validatedBy = ValidTransactionValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidTransaction {

  String message() default "Transaction must have exactly two parties: either user-user, user-external or external-user";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
