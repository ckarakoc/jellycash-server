package nl.ckarakoc.jellycash.dto.api.v1.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "A generic category dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

  @Schema(description = "The id of the category", example = "1")
  @NotBlank
  private Long categoryId;

  @Schema(description = "The unique name of the category", example = "Entertainment")
  @NotBlank
  private String name;
}
