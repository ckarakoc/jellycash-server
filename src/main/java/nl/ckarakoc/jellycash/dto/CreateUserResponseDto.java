package nl.ckarakoc.jellycash.dto;

import lombok.Data;

@Data
public class CreateUserResponseDto {
	private String username;
	private String email;
	private String firstName;
	private String lastName;
}
