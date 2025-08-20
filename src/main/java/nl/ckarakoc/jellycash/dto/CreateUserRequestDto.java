package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ckarakoc.jellycash.validator.Password;
import nl.ckarakoc.jellycash.validator.PasswordMatches;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class CreateUserRequestDto {
	@NotNull
	@NotBlank
	@Size(max = 255)
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Password
	private String password;

	@NotBlank
	@Password
	private String confirmPassword;

	@Size(max = 255)
	private String firstName;

	@Size(max = 255)
	private String lastName;

	@Size(max = 255)
	private String avatar;

	@Size(min = 3, max = 3)
	@Pattern(regexp = "[A-Z]{3}")
	private String currency = "EUR";
}
