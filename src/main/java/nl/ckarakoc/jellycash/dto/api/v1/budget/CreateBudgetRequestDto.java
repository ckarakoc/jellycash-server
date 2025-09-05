package nl.ckarakoc.jellycash.dto.api.v1.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The request for a create budget operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBudgetRequestDto {

  @Schema(description = "The amount in the budget", example = "100")
  @NotNull
  @PositiveOrZero
  @Digits(integer = 10, fraction = 2)
  private BigDecimal amount;
  @Schema(description = "The target amount in the budget", example = "1000")
  @NotNull
  @PositiveOrZero
  @Digits(integer = 10, fraction = 2)
  private BigDecimal targetAmount;
  @Schema(description = "The color of the budget", example = "#40E0D0")
  @NotBlank
  private String color;
  @Schema(description = "The due date of the budget in ISO-8601 date-time format", example = "2026-01-01T00:00:00")
  @NotNull
  @Future
  private LocalDateTime dueDate;
}
