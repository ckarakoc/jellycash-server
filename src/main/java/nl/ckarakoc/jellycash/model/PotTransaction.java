package nl.ckarakoc.jellycash.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pot_transactions")
public class PotTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pot_transaction_id", nullable = false)
  private Long potTransactionId;

  private BigDecimal amount;
  private Boolean isCredit;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "pot_id")
  private Pot pot;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PotTransaction that)) {
      return false;
    }
    return Objects.equals(potTransactionId, that.potTransactionId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(potTransactionId);
  }
}
