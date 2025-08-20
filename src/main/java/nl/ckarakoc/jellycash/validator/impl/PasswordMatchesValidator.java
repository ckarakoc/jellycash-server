package nl.ckarakoc.jellycash.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.validator.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, CreateUserRequestDto> {

	@Override
	public boolean isValid(CreateUserRequestDto dto, ConstraintValidatorContext context) {
		if (dto.getPassword() == null || dto.getConfirmPassword() == null) return false;
		return dto.getPassword().equals(dto.getConfirmPassword());
	}

}
