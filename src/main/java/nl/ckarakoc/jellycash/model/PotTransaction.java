package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "pot_transactions")
public class PotTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pot_transaction_id", nullable = false)
	private Long potTransactionId;

	private Long amount;
	private Boolean isCredit;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "pot_id")
	private Pot pot;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PotTransaction that)) return false;
		return Objects.equals(potTransactionId, that.potTransactionId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(potTransactionId);
	}
}
