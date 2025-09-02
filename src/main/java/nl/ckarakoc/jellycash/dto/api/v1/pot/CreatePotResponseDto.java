package nl.ckarakoc.jellycash.dto.api.v1.pot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePotResponseDto {

  private Long potId;
}
