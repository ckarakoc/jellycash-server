package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pots")
public class Pot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pot_id", nullable = false)
	private Long potId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "pot")
	private List<PotTransaction> potTransactions;
}
