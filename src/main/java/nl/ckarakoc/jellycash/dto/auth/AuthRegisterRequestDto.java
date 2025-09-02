package nl.ckarakoc.jellycash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ckarakoc.jellycash.validator.Password;
import nl.ckarakoc.jellycash.validator.PasswordMatches;

@Schema(description = "The request for registering a new user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class AuthRegisterRequestDto {

  @Schema(description = "The email address of the user", example = "mark@rutte.nl")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "The password of the user", example = "passWord@123")
  @NotBlank
  @Password
  private String password;

  @Schema(description = "The password confirmation of the user", example = "passWord@123")
  @NotBlank
  @Password
  private String confirmPassword;

  @Schema(description = "The first name of the user", example = "Mark")
  @Size(max = 255)
  private String firstName;

  @Schema(description = "The last name of the user", example = "Rutte")
  @Size(max = 255)
  private String lastName;

  @Schema(description = "The avatar of the user", example = "avatar.png")
  @Size(max = 255)
  private String avatar;

  @Schema(description = "The currency of the user", example = "EUR")
  @Size(min = 3, max = 3)
  @Pattern(regexp = "[A-Z]{3}")
  @Builder.Default
  private String currency = "EUR";
}
