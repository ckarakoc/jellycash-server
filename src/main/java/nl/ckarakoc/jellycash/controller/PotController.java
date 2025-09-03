package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants.ApiPaths;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotResponseDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.UpdatePotRequestDto;
import nl.ckarakoc.jellycash.exception.CreationException;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.service.PotService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.Pots)
@Tag(name = "Pots", description = "Operations related to pots, including pot transactions.")
public class PotController {

  private final PotService potService;
  private final ModelMapper modelMapper;

  @Operation(
      summary = "List all pots",
      description = "Retrieves all available pots",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid request")
      }
  )
  @GetMapping
  public ResponseEntity getAllPots() {
    throw new NotImplementedException();
  }

  @Operation(
      summary = "Get pot by ID",
      description = "Retrieves a specific pot by its ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Pot not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity getPot(@PathVariable @Schema(description = "ID of the pot") Long id) {
    throw new NotImplementedException();
  }

  @Operation(
      summary = "Create a new pot",
      description = "Creates a new pot with the provided data",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created"),
          @ApiResponse(responseCode = "400", description = "Bad request: Missing or invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PostMapping
  public ResponseEntity<CreatePotResponseDto> createPot(
      @RequestBody @Valid CreatePotRequestDto dto,
      @AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(401).build();
    }

    // TODO: Write test
    Pot created = potService.createPot(dto, user)
        .orElseThrow(() -> new CreationException(Pot.class, "Failed to create pot"));

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getPotId())
        .toUri();

    return ResponseEntity.created(location).body(modelMapper.map(created, CreatePotResponseDto.class));
  }

  @Operation(
      summary = "Fully update a pot",
      description = "Updates an existing pot by replacing all its data",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Pot not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity updatePot(
      @PathVariable @Schema(description = "ID of the pot") Long id,
      @RequestBody @Valid UpdatePotRequestDto dto) {
    throw new NotImplementedException();
    //potService.updatePot(dto, user);
  }

  @Operation(
      summary = "Partially update a pot",
      description = "Updates one or more fields of an existing pot",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Pot not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity partialUpdatePot(@PathVariable @Schema(description = "ID of the pot") Long id) {
    throw new NotImplementedException();
  }

  @Operation(
      summary = "Delete a pot",
      description = "Deletes a pot by its ID",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content: Pot deleted successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Pot not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePot(@PathVariable @Schema(description = "ID of the pot") Long id) {
    throw new NotImplementedException();
  }
}
