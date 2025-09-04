package nl.ckarakoc.jellycash.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.ckarakoc.jellycash.validator.ValidTransaction;
import org.hibernate.annotations.CreationTimestamp;

@ValidTransaction
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id", nullable = false)
  private Long transactionId;

  private Long amount;
  private Boolean isCredit;
  private Boolean isRecurring;
  private String externalParty = null;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private User sender;

  @ManyToOne
  @JoinColumn(name = "recipient_id")
  private User recipient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recurring_bill_id")
  @ToString.Exclude
  private RecurringBill recurringBill;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Transaction that)) {
      return false;
    }
    return Objects.equals(transactionId, that.transactionId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(transactionId);
  }
}
