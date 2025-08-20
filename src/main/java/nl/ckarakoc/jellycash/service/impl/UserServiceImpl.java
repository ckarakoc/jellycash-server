package nl.ckarakoc.jellycash.service.impl;

import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
		//todo: handle validation errors in the global exception handler
		User created = userRepository.save(modelMapper.map(createUserRequestDto, User.class));
		return modelMapper.map(created, CreateUserResponseDto.class);
	}
}
