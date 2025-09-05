package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants.ApiPaths;
import nl.ckarakoc.jellycash.dto.api.v1.budget.BudgetDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.CreateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.PartialUpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.UpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.model.Budget;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.service.BudgetService;
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
@RequestMapping(ApiPaths.Budgets)
@Tag(name = "Budgets", description = "Operations related to budgets")
public class BudgetController {

  private final ModelMapper modelMapper;
  private final BudgetService budgetService;

  @Operation(
      summary = "List all budgets",
      description = "Retrieves all available budgets of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid request")
      }
  )
  @GetMapping
  public ResponseEntity<List<BudgetDto>> getAllBudgets(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(budgetService.getAllBudgets(user).stream().map(budget -> modelMapper.map(budget, BudgetDto.class)).toList());
  }

  @Operation(
      summary = "Get budget by ID",
      description = "Retrieves a specific budget by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Budget not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<BudgetDto> getBudget(
      @PathVariable @Schema(description = "ID of the budget") Long id,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(modelMapper.map(budgetService.getBudget(id, user), BudgetDto.class));
  }

  @Operation(
      summary = "Create a new budget",
      description = "Creates a new budget with the provided data for the logged in user",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created"),
          @ApiResponse(responseCode = "400", description = "Bad request: Missing or invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PostMapping
  public ResponseEntity<Void> createBudget(
      @RequestBody @Valid CreateBudgetRequestDto dto,
      @AuthenticationPrincipal User user) {
    Budget created = budgetService.createBudget(dto, user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getBudgetId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(
      summary = "Fully update a budget",
      description = "Updates an existing budget by replacing all its data of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Budget not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity<BudgetDto> updateBudget(
      @PathVariable @Schema(description = "ID of the budget", example = "6") Long id,
      @RequestBody @Valid UpdateBudgetRequestDto dto,
      @AuthenticationPrincipal User user) {

    return ResponseEntity.ok(modelMapper.map(budgetService.updateBudget(id, dto, user), BudgetDto.class));
  }

  @Operation(
      summary = "Partially update a budget",
      description = "Updates one or more fields of an existing budget of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Budget not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity<BudgetDto> partialUpdateBudget(
      @PathVariable @Schema(description = "ID of the budget") Long id,
      @RequestBody @Valid PartialUpdateBudgetRequestDto dto,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(modelMapper.map(budgetService.partialUpdateBudget(id, dto, user), BudgetDto.class));
  }

  @Operation(
      summary = "Delete a budget",
      description = "Deletes a budget by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content: Budget deleted successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Budget not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBudget(
      @PathVariable @Schema(description = "ID of the budget") Long id,
      @AuthenticationPrincipal User user) {
    budgetService.deleteBudget(id, user);
    return ResponseEntity.noContent().build();
  }

}
