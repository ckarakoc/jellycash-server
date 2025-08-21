package nl.ckarakoc.jellycash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link nl.ckarakoc.jellycash.model.Pot}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePotRequestDto {
	@NotBlank
	private String name;
	@NotNull
	@Positive
	private Long maxBalance;
}