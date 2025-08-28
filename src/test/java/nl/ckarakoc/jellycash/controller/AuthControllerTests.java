package nl.ckarakoc.jellycash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.AuthRegisterResponseDto;
import nl.ckarakoc.jellycash.exception.AuthenticationConflictException;
import nl.ckarakoc.jellycash.manager.AuthManager;
import nl.ckarakoc.jellycash.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AuthManager authManager;

	@MockitoBean
	private JwtService jwtService;

	@Test
	void shouldReturn200_whenValidRequestGiven_register() throws Exception {
		AuthRegisterRequestDto validRequest = AuthRegisterRequestDto.builder()
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();

		AuthRegisterResponseDto mockTokens = AuthRegisterResponseDto.builder()
			.accessToken("test_access_token")
			.refreshToken("test_refresh_token")
			.build();

		when(authManager.register(any())).thenReturn(mockTokens);

		MvcResult mvcResult = mockMvc.perform(post("/auth/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validRequest)))
			.andExpect(status().isOk())
			.andExpect(cookie().exists("access_token"))
			.andExpect(cookie().httpOnly("access_token", true))
			.andExpect(cookie().secure("access_token", true))
			.andExpect(cookie().path("access_token", "/"))
			.andExpect(cookie().maxAge("access_token", (int) AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY.getSeconds()))
			.andExpect(cookie().sameSite("access_token", "Strict"))
			.andExpect(cookie().exists("refresh_token"))
			.andExpect(cookie().httpOnly("refresh_token", true))
			.andExpect(cookie().secure("refresh_token", true))
			.andExpect(cookie().path("refresh_token", "/"))
			.andExpect(cookie().maxAge("refresh_token", (int) AppConstants.JwtTokenExpiry.REFRESH_TOKEN_EXPIRY.getSeconds()))
			.andExpect(cookie().sameSite("refresh_token", "Strict"))
			.andExpect(header().exists("Set-Cookie"))
			.andReturn();

		List<String> cookieHeaders = mvcResult.getResponse().getHeaders("Set-Cookie");
		assertThat(cookieHeaders).hasSize(2);
		assertThat(cookieHeaders).anyMatch(cookie -> cookie.contains("access_token"));
		assertThat(cookieHeaders).anyMatch(cookie -> cookie.contains("refresh_token"));
	}

	/*@Test
	public void shouldReturn403Forbidden_whenCsrfTokenIsMissing_register() throws Exception {
		AuthRegisterRequestDto validRequest = AuthRegisterRequestDto.builder()
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();

		AuthRegisterResponseDto mockTokens = AuthRegisterResponseDto.builder()
			.accessToken("test_access_token")
			.refreshToken("test_refresh_token")
			.build();

		when(authManager.register(any())).thenReturn(mockTokens);

		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validRequest)))
			.andExpect(status().isForbidden());
	}*/

	@Test
	public void shouldReturn409Conflict_whenUserAlreadyExists_register() throws Exception {
		AuthRegisterRequestDto validRequest = AuthRegisterRequestDto.builder()
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();

		when(authManager.register(any())).thenThrow(new AuthenticationConflictException("Email already exists"));

		mockMvc.perform(post("/auth/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validRequest)))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.status").exists())
			.andExpect(jsonPath("$.error").exists())
			.andExpect(jsonPath("$.details").exists())
			.andExpect(jsonPath("$.error").value("Authentication Error"))
			.andExpect(jsonPath("$.details").value("Email already exists"));
	}

	@CsvSource(value = {
		"mark@rutte.nl | Vergeten@123  | Vergeten@1234 | Mark  | Rutte | password",    // password: has not been repeated properly
		"mark@rutte.nl | vergeten@123  | vergeten@123  | Mark  | Rutte | password",    // password: no capital letters
		"mark@rutte.nl | Vergeten123   | Vergeten123   | Mark  | Rutte | password",    // password: no special characters
		"mark@rutte.nl | Vergeten@     | Vergeten@     | Mark  | Rutte | password",    // password: no numbers
		"mark@rutte.nl | Verg          | Verg          | Mark  | Rutte | password",    // password: too short
		"mark@rutte.nl | ''            | ''            | Mark  | Rutte | password",    // password: empty
		"mark@rutte.nl | NULL          | NULL          | Mark  | Rutte | password",    // password: null
		"mark@.nl      | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: wrong email
		"''            | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: empty
		"NULL          | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: null
	}, delimiter = '|', nullValues = {"NULL"})
	@ParameterizedTest
	public void shouldReturn400BadRequest_whenInvalidRequestGiven_register(
		String email,
		String password,
		String confirmPassword,
		String firstName,
		String lastName
	) throws Exception {
		AuthRegisterRequestDto mockRutte = AuthRegisterRequestDto.builder()
			.email(email)
			.password(password)
			.confirmPassword(confirmPassword)
			.firstName(firstName)
			.lastName(lastName)
			.build();

		MvcResult mvcResult = mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockRutte)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.status").exists())
			.andExpect(jsonPath("$.error").exists())
			.andExpect(jsonPath("$.details").exists())
			.andExpect(jsonPath("$.error").value("Validation Error"))
			.andReturn();

		System.out.println("Response JSON: " + mvcResult.getResponse().getContentAsString());
	}

	public void shouldReturn200_whenValidRequestGiven_login() throws Exception {}
	public void shouldReturn401Unauthorized_whenInvalidCredentials_login() throws Exception {}
	public void shouldReturn403Forbidden_whenAccountIsLocked_login() throws Exception {}

	public void shouldReturn200_whenValidRequestGiven_logout() throws Exception {}
	public void shouldReturn401Unauthorized_whenInvalidToken_logout() throws Exception {}

	public void shouldReturn200_whenValidRequestGiven_refresh() throws Exception {}
	public void shouldReturn401Unauthorized_whenInvalidRefreshToken_refresh() throws Exception {}
}
