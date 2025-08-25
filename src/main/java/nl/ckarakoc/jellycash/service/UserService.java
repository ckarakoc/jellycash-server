package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.dto.UpdateUserRequestDto;
import nl.ckarakoc.jellycash.dto.UpdateUserResponseDto;
import nl.ckarakoc.jellycash.model.User;

public interface UserService {
	CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

	UpdateUserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto);

	boolean existsByEmail(String email);

	User findByEmail(String email);

	User save(User user);
}
