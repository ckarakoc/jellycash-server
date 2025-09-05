package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link nl.ckarakoc.jellycash.model.User}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {

  private String firstName;
  private String lastName;
  private String avatar;
  private String currency;
  @Digits(integer = 10, fraction = 2)
  private BigDecimal balance;
  @Digits(integer = 10, fraction = 2)
  private BigDecimal income;
  @Digits(integer = 10, fraction = 2)
  private BigDecimal expenses;
}
