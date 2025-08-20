package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
	public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);
}
