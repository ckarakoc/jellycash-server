package nl.ckarakoc.jellycash.service.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.dto.UpdateUserRequestDto;
import nl.ckarakoc.jellycash.dto.UpdateUserResponseDto;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
		//todo: handle validation errors in the global exception handler
//		User user = modelMapper.map(createUserRequestDto, User.class);
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		User created = userRepository.save(user);
//		return modelMapper.map(created, CreateUserResponseDto.class);
		throw new NotImplementedException();
	}

	@Override
	public UpdateUserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto) {
		throw new NotImplementedException();
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
