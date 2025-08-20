package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false)
	private Long transactionId;

	private Long amount;
	private Boolean isCredit;
	private Boolean isRecurring;

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

	@ManyToOne
	@JoinColumn(name = "recurring_bill_id")
	private RecurringBill recurringBill;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Transaction that)) return false;
		return Objects.equals(transactionId, that.transactionId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(transactionId);
	}
}
