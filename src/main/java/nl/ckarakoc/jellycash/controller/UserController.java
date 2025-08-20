package nl.ckarakoc.jellycash.controller;

import jakarta.validation.Valid;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/users")
	public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
		return new ResponseEntity<>(userService.createUser(createUserRequestDto), HttpStatus.CREATED);
	}

}
