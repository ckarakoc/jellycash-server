package nl.ckarakoc.jellycash.dto.api.v1.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "A generic transaction dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

  private Long transactionId;

  private BigDecimal amount;
  //private Boolean isCredit;
  private String externalParty = null;
  private Long recipientId;
}
