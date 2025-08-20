package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "recurring_bills")
public class RecurringBill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recurring_bill_id", nullable = false)
	private Long recurringBillId;

	@OneToMany(mappedBy = "recurringBill")
	private List<Transaction> transactions;
}
