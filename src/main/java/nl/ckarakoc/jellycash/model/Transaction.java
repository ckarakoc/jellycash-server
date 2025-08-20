package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false)
	private Long transactionId;

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
}
