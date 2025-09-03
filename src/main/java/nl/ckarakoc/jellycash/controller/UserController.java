package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.ckarakoc.jellycash.config.AppConstants.ApiPaths;
import nl.ckarakoc.jellycash.dto.CreateUserRequestDto;
import nl.ckarakoc.jellycash.dto.CreateUserResponseDto;
import nl.ckarakoc.jellycash.dto.UpdateUserRequestDto;
import nl.ckarakoc.jellycash.dto.UpdateUserResponseDto;
import nl.ckarakoc.jellycash.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: test it out
// TODO: Proper Swagger documentation

@RestController
@RequestMapping(ApiPaths.Users)
@Tag(name = "User Controller", description = "Operations related to users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "Get all users", description = "Returns a list of all users in the system")
  @ApiResponse(responseCode = "200", description = "User created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid user data")
  public String getAllUsers() {
    return "All users";
  }

  @GetMapping("/active")
  @Operation(summary = "Get active users", description = "Returns only users with active status")
  public String getActiveUsers() {
    return "Active users";
  }

  @PostMapping
  public ResponseEntity<CreateUserResponseDto> createUser(
      @RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
    return new ResponseEntity<>(userService.createUser(createUserRequestDto), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<UpdateUserResponseDto> updateUser(
      @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto) {
    return new ResponseEntity<>(userService.updateUser(updateUserRequestDto), HttpStatus.OK);
  }
}
