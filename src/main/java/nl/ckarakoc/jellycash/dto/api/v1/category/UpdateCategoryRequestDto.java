package nl.ckarakoc.jellycash.dto.api.v1.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The request for a update category operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequestDto {

  @Schema(description = "The unique name of the category", example = "Entertainment")
  @NotBlank
  private String name;
}
