package nl.ckarakoc.jellycash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ckarakoc.jellycash.validator.Password;

@Schema(description = "The request for logging in a user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLoginRequestDto {

  @Schema(description = "The email address of the user", example = "mark@rutte.nl")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "The password of the user", example = "geenHerinneringen$44")
  @NotBlank
  @Password
  private String password;
}
