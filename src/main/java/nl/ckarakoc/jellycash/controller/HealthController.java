package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.ckarakoc.jellycash.config.AppConstants.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

  @Operation(
      summary = "health",
      description = "Health check endpoint",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success")
      }
  )
  @GetMapping("/health")
  public ResponseEntity<Void> health() {
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "health: pots",
      description = "Health check endpoint: pots",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success")
      }
  )
  @GetMapping(ApiPaths.Pots + "/health")
  public ResponseEntity<Void> potsHealth() {
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "health: users",
      description = "Health check endpoint: users",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success")
      }
  )
  @GetMapping(ApiPaths.Users + "/health")
  public ResponseEntity<Void> usersHealth() {
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "health: auth",
      description = "Health check endpoint: auth",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success")
      }
  )
  @GetMapping(ApiPaths.AUTH + "/health")
  public ResponseEntity<Void> authHealth() {
    return ResponseEntity.ok().build();
  }
}
