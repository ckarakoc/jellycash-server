package nl.ckarakoc.jellycash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
	private String firstName;
	private String lastName;
	private String avatar;
	private String currency;
	private Long balance;
	private Long income;
	private Long expenses;
}
