package nl.ckarakoc.jellycash.dto.api.v1.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The request for a partial update category operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartialUpdateCategoryRequestDto {

  @Schema(description = "The unique name of the category", example = "Entertainment")
  private String name;
}
