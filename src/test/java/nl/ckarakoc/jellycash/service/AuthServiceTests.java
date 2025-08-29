package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.AuthRegisterResponseDto;
import nl.ckarakoc.jellycash.exception.AuthenticationConflictException;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {

	@Autowired
	private AuthService authService;
	//	@MockitoBean
	//	private ModelMapper modelMapper;
	//	@MockitoBean
	//	private PasswordEncoder passwordEncoder;
	@MockitoBean
	private JwtService jwtService;
	@MockitoBean
	private UserService userService;
	@MockitoBean
	private RoleService roleService;
	@MockitoBean
	private AuthenticationManager authenticationManager;
	@MockitoBean
	private RefreshTokenRepository refreshTokenRepository;


	@Test
	void shouldThrowConflict_whenEmailAlreadyExists() {
		AuthRegisterRequestDto dto = AuthRegisterRequestDto.builder()
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();

		when(userService.existsByEmail(dto.getEmail())).thenReturn(true);

		assertThatThrownBy(() -> authService.register(dto))
			.isInstanceOf(AuthenticationConflictException.class)
			.hasMessageContaining("Email already exists");
	}

	@Test
	public void shouldReturnTokens_whenValidRequest_register() {
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


		when(userService.existsByEmail(dto.getEmail())).thenReturn(false);
		when(roleService.getRole(AppRole.USER)).thenReturn(role);
		when(userService.save(any())).thenReturn(user);
		when(jwtService.generateToken(any())).thenReturn("access-token");
		when(jwtService.generateRefreshToken(any())).thenReturn(refresh);
		when(refreshTokenRepository.save(any())).thenReturn(refresh);

		AuthRegisterResponseDto tokens = authService.register(dto);

		assertThat(tokens.getAccessToken()).isEqualTo("access-token");
		assertThat(tokens.getRefreshToken()).isEqualTo("refresh-token-123");
		verify(userService).save(any(User.class));
		verify(refreshTokenRepository).save(refresh);
	}
}
