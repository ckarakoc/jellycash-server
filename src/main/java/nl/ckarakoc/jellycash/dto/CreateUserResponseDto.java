package nl.ckarakoc.jellycash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link nl.ckarakoc.jellycash.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponseDto {

  private String email;
  private String firstName;
  private String lastName;
}
