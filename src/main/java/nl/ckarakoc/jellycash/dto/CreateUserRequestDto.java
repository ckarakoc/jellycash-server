package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ckarakoc.jellycash.validator.Password;

/**
 * DTO for {@link nl.ckarakoc.jellycash.model.User}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Password
  private String password;

  @Size(max = 255)
  private String firstName;

  @Size(max = 255)
  private String lastName;

  @Size(max = 255)
  private String avatar;

  @Size(min = 3, max = 3)
  @Pattern(regexp = "[A-Z]{3}")
  @Builder.Default
  private String currency = "EUR";
}
