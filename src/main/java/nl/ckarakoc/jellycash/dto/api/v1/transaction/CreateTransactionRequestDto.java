package nl.ckarakoc.jellycash.dto.api.v1.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.ckarakoc.jellycash.model.Category;
import nl.ckarakoc.jellycash.model.RecurringBill;
import nl.ckarakoc.jellycash.model.User;
import org.hibernate.annotations.CreationTimestamp;

@Schema(description = "The request for a create transaction operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransactionRequestDto {

  private BigDecimal amount;
  //private Boolean isCredit;
  // TODO: externalParty OR recipientId NOT both
  private String externalParty = null;
  private Long recipientId;
}
