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
@Table(name = "pots")
public class Pot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pot_id", nullable = false)
	private Long potId;

	private String name;
	private Long balance;
	private Long maxBalance;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "pot")
	private List<PotTransaction> potTransactions;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pot pot)) return false;
		return Objects.equals(potId, pot.potId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(potId);
	}
}
