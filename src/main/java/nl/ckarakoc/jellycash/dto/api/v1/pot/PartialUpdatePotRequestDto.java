package nl.ckarakoc.jellycash.dto.api.v1.pot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The request for a partial update pot operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartialUpdatePotRequestDto {

  @Schema(description = "The name of the pot", example = "Alien Abduction Insurance")
  private String name = null;

  @Schema(description = "The balance of the pot", example = "42")
  @PositiveOrZero
  private Long balance = null;

  @Schema(description = "The maximum balance of the pot", example = "1000")
  @PositiveOrZero
  private Long maxBalance = null;
}

