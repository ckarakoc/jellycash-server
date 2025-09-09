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
import nl.ckarakoc.jellycash.dto.api.v1.transaction.CreateTransactionRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.transaction.TransactionDto;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.model.Transaction;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.service.TransactionService;
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
@RequestMapping(ApiPaths.Transactions)
@Tag(name = "Transactions", description = "Operations related to transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final ModelMapper modelMapper;

  @Operation(
      summary = "List all transactions",
      description = "Retrieves all available transactions of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid request")
      }
  )
  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(transactionService.getAllTransactions(user).stream().map(transaction -> modelMapper.map(transaction, TransactionDto.class)).toList());
  }

  @Operation(
      summary = "Get transaction by ID",
      description = "Retrieves a specific transaction by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Transaction not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<TransactionDto> getTransaction(
      @PathVariable @Schema(description = "ID of the transaction") Long id,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(modelMapper.map(transactionService.getTransaction(id, user), TransactionDto.class));
  }

  @Operation(
      summary = "Create a new transaction",
      description = "Creates a new transaction with the provided data for the logged in user",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created"),
          @ApiResponse(responseCode = "400", description = "Bad request: Missing or invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PostMapping
  public ResponseEntity<Void> createTransaction(
      @RequestBody @Valid CreateTransactionRequestDto dto,
      @AuthenticationPrincipal User user) {
    Transaction created = transactionService.createTransaction(dto, user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getTransactionId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(
      summary = "Fully update a transaction",
      description = "Updates an existing transaction by replacing all its data of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Transaction not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity<TransactionDto> updateTransaction(
      @PathVariable @Schema(description = "ID of the transaction", example = "1") Long id) {
    throw new NotImplementedException();
  }

  @Operation(
      summary = "Partially update a transaction",
      description = "Updates one or more fields of an existing transaction of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Transaction not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity<TransactionDto> partialUpdateTransaction(
      @PathVariable @Schema(description = "ID of the transaction") Long id) {
    throw new NotImplementedException();
  }

  @Operation(
      summary = "Delete a transaction",
      description = "Deletes a transaction by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content: Transaction deleted successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Transaction not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTransaction(
      @PathVariable @Schema(description = "ID of the transaction") Long id,
      @AuthenticationPrincipal User user) {
    // transactionService.deleteTransaction(id, user);
    // return ResponseEntity.noContent().build();
    throw new NotImplementedException();
  }
}
