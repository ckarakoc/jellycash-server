package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pot_transactions")
public class PotTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pot_transaction_id", nullable = false)
	private Long potTransactionId;

	@ManyToOne
	@JoinColumn(name = "pot_id")
	private Pot pot;
}
