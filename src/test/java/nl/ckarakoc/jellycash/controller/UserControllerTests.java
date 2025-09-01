package nl.ckarakoc.jellycash.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.security.service.JwtService;
import nl.ckarakoc.jellycash.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests extends BaseControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockitoBean
  private UserService userService;
  @MockitoBean
  private JwtService jwtService;

  CreateUserRequestDto mockRutte;

  static Stream<Arguments> provideInvalidUserArguments() {
    String SUPER_LONG_F = "F".repeat(300); // realistic max length
    return Stream.of(
        Arguments.of(SUPER_LONG_F, "Vergeten@123", "Mark", "Rutte", "email"),
        // email: max-length
        Arguments.of("mark@rutte.nl", SUPER_LONG_F, "Mark", "Rutte", "password"),
        // password: max-length
        Arguments.of("mark@rutte.nl", "Vergeten@123", SUPER_LONG_F, "Rutte", "firstName"),
        // firstName: max-length
        Arguments.of("mark@rutte.nl", "Vergeten@123", "Mark", SUPER_LONG_F, "lastName")
        // lastName: max-length
    );
  }

  @BeforeEach
  public void setUp() {
    mockRutte = CreateUserRequestDto
        .builder()
        .email("mark@rutte.nl")
        .password("Vergeten@123")
        .firstName("Mark")
        .lastName("Rutte")
        .build();
  }

  @Test
  public void shouldCreateUser_whenValidRequestGiven() throws Exception {
    String jsonRutte = objectMapper.writeValueAsString(mockRutte);
    CreateUserResponseDto responseDto = CreateUserResponseDto.builder()
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
        .andExpect(jsonPath("$.email").value("mark@rutte.nl"))
        .andExpect(jsonPath("$.firstName").value("Mark"))
        .andExpect(jsonPath("$.lastName").value("Rutte"))
        .andReturn();

    System.out.println("Response JSON: " + mvcResult.getResponse().getContentAsString());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "mark@rutte.nl | vergeten@123  | Mark  | Rutte | password",    // password: no capital letters
      "mark@rutte.nl | Vergeten123   | Mark  | Rutte | password",
      // password: no special characters
      "mark@rutte.nl | Vergeten@     | Mark  | Rutte | password",    // password: no numbers
      "mark@rutte.nl | Verg          | Mark  | Rutte | password",    // password: too short
      "mark@rutte.nl | ''            | Mark  | Rutte | password",    // password: empty
      "mark@rutte.nl | NULL          | Mark  | Rutte | password",    // password: null
      "mark@.nl      | Vergeten@123  | Mark  | Rutte | email",       // email: wrong email
      "''            | Vergeten@123  | Mark  | Rutte | email",       // email: empty
      "NULL          | Vergeten@123  | Mark  | Rutte | email",       // email: null
  }, delimiter = '|', nullValues = {"NULL"})
  @MethodSource("provideInvalidUserArguments")
  public void shouldReturnBadRequest_whenInvalidRequestGiven(String email, String password,
      String firstName, String lastName, String check) throws Exception {
    mockRutte = CreateUserRequestDto.builder()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName).build();

    MvcResult mvcResult = mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRutte)))
        .andExpect(status().isBadRequest())
        .andReturn();

    System.out.println("Response JSON: " + mvcResult.getResponse().getContentAsString());
  }

}