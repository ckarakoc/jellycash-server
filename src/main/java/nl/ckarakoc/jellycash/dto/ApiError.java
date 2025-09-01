package nl.ckarakoc.jellycash.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiError {

  private final LocalDateTime timestamp;
  private final int status;
  private final String error;
  private final Object details;

  public ApiError(int status, String error, Object details) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.error = error;
    this.details = details;
  }
}
