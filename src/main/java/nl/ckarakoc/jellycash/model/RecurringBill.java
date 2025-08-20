package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "recurring_bills")
public class RecurringBill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recurring_bill_id", nullable = false)
	private Long recurringBillId;

	private String name;
	private Long amount;
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
	private List<Transaction> transactions;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RecurringBill that)) return false;
		return Objects.equals(recurringBillId, that.recurringBillId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(recurringBillId);
	}
}
