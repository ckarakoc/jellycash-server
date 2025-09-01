package nl.ckarakoc.jellycash.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import nl.ckarakoc.jellycash.validator.JwtToken;

public class JwtTokenValidator implements ConstraintValidator<JwtToken, String> {

  private static final Pattern JWT_PATTERN = Pattern.compile(
      "^[A-Za-z0-9_-]+={0,2}\\.[A-Za-z0-9_-]+={0,2}\\.[A-Za-z0-9_-]+={0,2}$");

  @Override
  public boolean isValid(String token, ConstraintValidatorContext context) {
    return token != null && JWT_PATTERN.matcher(token).matches();
  }
}
