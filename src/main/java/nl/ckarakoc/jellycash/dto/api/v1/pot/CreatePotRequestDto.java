package nl.ckarakoc.jellycash.dto.api.v1.pot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePotRequestDto {

  @Schema(description = "The name of the pot", example = "")
  @NotBlank
  private String name;

  @Schema(description = "The balance of the pot")
  @NotNull
  @PositiveOrZero
  private Long balance;

  @Schema(description = "The maximum balance of the pot")
  @NotNull
  @PositiveOrZero
  private Long maxBalance;
}