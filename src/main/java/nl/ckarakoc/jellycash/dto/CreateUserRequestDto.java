package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.ckarakoc.jellycash.validator.Password;
import nl.ckarakoc.jellycash.validator.PasswordMatches;

@Data
@PasswordMatches
public class CreateUserRequestDto {
	@NotNull
	@NotBlank
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Password
	private String password;

	@NotBlank
	private String repeatedPassword;

	private String firstName;
	private String lastName;
	private String avatar;
	private String currency = "EUR";
}
