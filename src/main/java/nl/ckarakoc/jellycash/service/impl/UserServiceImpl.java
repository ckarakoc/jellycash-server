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

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
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
}
