package nl.ckarakoc.jellycash.dto.api.v1.pot;

import jakarta.validation.constraints.NotBlank;

public class UpdatePotRequestDto {

  @NotBlank
  private String name;
  @NotBlank
  private Long balance;
  @NotBlank
  private Long maxBalance;
}
