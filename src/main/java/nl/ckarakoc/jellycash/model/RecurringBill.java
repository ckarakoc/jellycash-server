package nl.ckarakoc.jellycash.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "recurring_bills")
public class RecurringBill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recurring_bill_id", nullable = false)
  private Long recurringBillId;

  private String name;
  private BigDecimal amount;
  private Boolean isCredit;
  private Boolean isDone;
  private LocalDateTime dueDate;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "recurringBill")
  @ToString.Exclude
  private List<Transaction> transactions;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof RecurringBill that)) {
      return false;
    }
    return Objects.equals(recurringBillId, that.recurringBillId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(recurringBillId);
  }
}
