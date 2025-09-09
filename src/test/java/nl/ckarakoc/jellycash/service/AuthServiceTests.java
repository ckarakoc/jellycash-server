package nl.ckarakoc.jellycash.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthTokenResponseDto;
import nl.ckarakoc.jellycash.exception.AuthenticationConflictException;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import nl.ckarakoc.jellycash.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTests extends BaseServiceTest {

  @Mock
  private JwtService jwtService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;


  @Spy
  private ModelMapper modelMapper = new ModelMapper();
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private RefreshTokenRepository refreshTokenRepository;
  @Mock
  private CompromisedPasswordChecker compromisedPasswordChecker;

  @InjectMocks
  private AuthServiceImpl authService;

  @Nested
  class RegisterTests {

    @Test
    void shouldThrowConflict_whenEmailAlreadyExists() {
      AuthRegisterRequestDto dto = AuthRegisterRequestDto.builder()
          .email("mark@rutte.nl")
          .password("Vergeten@123")
          .confirmPassword("Vergeten@123")
          .firstName("Mark")
          .lastName("Rutte")
          .build();

      when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

      assertThatThrownBy(() -> authService.register(dto))
          .isInstanceOf(AuthenticationConflictException.class)
          .hasMessageContaining("Email already exists");
    }

    @Test
    void shouldReturnTokens_whenValidRequest() {
      AuthRegisterRequestDto dto = AuthRegisterRequestDto.builder()
          .email("mark@rutte.nl")
          .password("Vergeten@123")
          .confirmPassword("Vergeten@123")
          .firstName("Mark")
          .lastName("Rutte")
          .build();

      User user = new User();
      user.setEmail(dto.getEmail());

      Role role = new Role();
      role.setRole(AppRole.USER);

      RefreshToken refresh = new RefreshToken();
      refresh.setToken("refresh-token-123");
      refresh.setExpiryDate(new Date());

      when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
      when(roleRepository.findByRole(AppRole.USER)).thenReturn(Optional.of(role));
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(jwtService.generateToken(any(User.class))).thenReturn("access-token");
      when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refresh);
      when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refresh);
      when(compromisedPasswordChecker.check(any(String.class))).thenReturn(new CompromisedPasswordDecision(false));

      AuthTokenResponseDto tokens = authService.register(dto);

      assertThat(tokens.getAccessToken()).isEqualTo("access-token");
      assertThat(tokens.getRefreshToken()).isEqualTo("refresh-token-123");

      verify(userRepository).save(any(User.class));
      verify(refreshTokenRepository).save(refresh);
    }
  }
}

