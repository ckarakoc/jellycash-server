package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ckarakoc.jellycash.validator.Password;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLoginRequestDto {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Password
  private String password;
}
