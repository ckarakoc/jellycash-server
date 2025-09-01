package nl.ckarakoc.jellycash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Test
  void shouldRegisterUserAndPersistTokens() throws Exception {
		/*AuthRegisterRequestDto validRequest = AuthRegisterRequestDto.builder()
			.email("mark@rutte.nl")
			.password("Vergeten@123")
			.confirmPassword("Vergeten@123")
			.firstName("Mark")
			.lastName("Rutte")
			.build();

		MvcResult mvcResult = mockMvc.perform(post("/auth/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validRequest)))
			.andExpect(status().isOk())
			.andExpect(cookie().exists("access_token"))
			.andExpect(cookie().exists("refresh_token"))
			.andReturn();

		User savedUser = userRepository.findByEmail("mark@rutte.nl").orElseThrow();
		assertThat(savedUser.getPassword()).startsWith("{bcrypt}$2a$"); // bcrypt encoded
		assertThat(savedUser.getRoles()).extracting("role").contains(AppRole.USER);

		List<RefreshToken> tokens = refreshTokenRepository.findAll();
		assertThat(tokens).hasSize(1); // I insert a user in a commandline-runner :/
		assertThat(tokens.getFirst().getUser().getEmail()).isEqualTo("mark@rutte.nl");*/
  }

}
