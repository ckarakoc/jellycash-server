package nl.ckarakoc.jellycash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockitoBean
	private UserService userService;
	CreateUserRequestDto mockRutte;

	static Stream<Arguments> provideInvalidUserArguments() {
		String SUPER_LONG_F = "F".repeat(300); // realistic max length
		return Stream.of(
			Arguments.of("mrutte", "mark@rutte.nl", SUPER_LONG_F, SUPER_LONG_F, "Mark", "Rutte", "password"),        // password: max-length
			Arguments.of("mrutte", SUPER_LONG_F, "Vergeten@123", "Vergeten@123", "Mark", "Rutte", "email"),                  // email: max-length
			Arguments.of(SUPER_LONG_F, "mark@rutte.nl", "Vergeten@123", "Vergeten@123", "Mark", "Rutte", "username"),        // username: max-length
			Arguments.of("mrutte", "mark@rutte.nl", "Vergeten@123", "Vergeten@123", SUPER_LONG_F, "Rutte", "firstName"),     // firstName: max-length
			Arguments.of("mrutte", "mark@rutte.nl", "Vergeten@123", "Vergeten@123", "Mark", SUPER_LONG_F, "lastName")        // lastName: max-length
		);
	}

	@BeforeEach
	public void setUp() {
		mockRutte = CreateUserRequestDto
			.builder()
			.username("mrutte")
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();
	}

	@Test
	public void shouldCreateUser_whenValidRequestGiven() throws Exception {
		String jsonRutte = objectMapper.writeValueAsString(mockRutte);
		CreateUserResponseDto responseDto = CreateUserResponseDto.builder()
			.username("mrutte")
			.email("mark@rutte.nl")
			.firstName("Mark")
			.lastName("Rutte")
			.build();
		when(userService.createUser(any(CreateUserRequestDto.class)))
			.thenReturn(responseDto);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRutte))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.username").value("mrutte"))
			.andExpect(jsonPath("$.email").value("mark@rutte.nl"))
			.andExpect(jsonPath("$.firstName").value("Mark"))
			.andExpect(jsonPath("$.lastName").value("Rutte"))
			.andReturn();

		System.out.println("Response JSON: " + mvcResult.getResponse().getContentAsString());
	}

	@ParameterizedTest
	@CsvSource(value = {
		"mrutte | mark@rutte.nl | Vergeten@123  | Vergeten@1234 | Mark  | Rutte | password",    // password: has not been repeated properly
		"mrutte | mark@rutte.nl | vergeten@123  | vergeten@123  | Mark  | Rutte | password",    // password: no capital letters
		"mrutte | mark@rutte.nl | Vergeten123   | Vergeten123   | Mark  | Rutte | password",    // password: no special characters
		"mrutte | mark@rutte.nl | Vergeten@     | Vergeten@     | Mark  | Rutte | password",    // password: no numbers
		"mrutte | mark@rutte.nl | Verg          | Verg          | Mark  | Rutte | password",    // password: too short
		"mrutte | mark@rutte.nl | ''            | ''            | Mark  | Rutte | password",    // password: empty
		"mrutte | mark@rutte.nl | NULL          | NULL          | Mark  | Rutte | password",    // password: null
		"mrutte | mark@.nl      | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: wrong email
		"mrutte | ''            | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: empty
		"mrutte | NULL          | Vergeten@123  | Vergeten@123  | Mark  | Rutte | email",       // email: null
		"''     | mark@rutte.nl | Vergeten@123  | Vergeten@123  | Mark  | Rutte | username",    // username: empty
		"NULL   | mark@rutte.nl | Vergeten@123  | Vergeten@123  | Mark  | Rutte | username"     // username: null
	}, delimiter = '|', nullValues = {"NULL"})
	@MethodSource("provideInvalidUserArguments")
	public void shouldReturnBadRequest_whenInvalidRequestGiven(String username, String email, String password, String repeatedPassword, String firstName, String lastName, String check) throws Exception {
		mockRutte = CreateUserRequestDto.builder()
			.username(username)
			.email(email)
			.password(password)
			.confirmPassword(repeatedPassword)
			.firstName(firstName)
			.lastName(lastName).build();

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockRutte)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").isMap())
			.andExpect(jsonPath("$.errors." + check).exists())
			.andReturn();

		System.out.println("Response JSON: " + mvcResult.getResponse().getContentAsString());
	}

}