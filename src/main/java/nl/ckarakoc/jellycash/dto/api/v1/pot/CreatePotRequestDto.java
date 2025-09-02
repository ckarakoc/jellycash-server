package nl.ckarakoc.jellycash.dto.api.v1.pot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePotRequestDto {

  @NotBlank
  private String name;

  @NotNull
  @Positive
  private Long balance = 0L;

  @NotNull
  @Positive
  private Long maxBalance;
}