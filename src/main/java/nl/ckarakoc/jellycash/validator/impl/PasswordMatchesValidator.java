package nl.ckarakoc.jellycash.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.ckarakoc.jellycash.dto.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.validator.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AuthRegisterRequestDto> {
	@Override
	public boolean isValid(AuthRegisterRequestDto dto, ConstraintValidatorContext context) {
		if (dto.getPassword() == null && dto.getConfirmPassword() == null) return true;
		if (dto.getPassword() == null || dto.getConfirmPassword() == null) return false;
		return dto.getPassword().equals(dto.getConfirmPassword());
	}
}
