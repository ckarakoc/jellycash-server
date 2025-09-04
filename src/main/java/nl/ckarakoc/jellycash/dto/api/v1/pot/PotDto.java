package nl.ckarakoc.jellycash.dto.api.v1.pot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "A generic pot dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PotDto {

  @Schema(description = "The id of the pot", example = "6")
  @NotBlank
  private Long potId;

  @Schema(description = "The name of the pot", example = "Alien Abduction Insurance")
  @NotBlank
  private String name;

  @Schema(description = "The balance of the pot", example = "42")
  @NotNull
  @PositiveOrZero
  private Long balance;

  @Schema(description = "The maximum balance of the pot", example = "1000")
  @NotNull
  @PositiveOrZero
  private Long maxBalance;
}