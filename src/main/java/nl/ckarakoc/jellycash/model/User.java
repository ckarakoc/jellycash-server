package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User user)) return false;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}

	@OneToMany(mappedBy = "user")
	private List<Pot> pots;

	@OneToMany(mappedBy = "user")
	private List<Budget> budgets;

	@OneToMany(mappedBy = "sender")
	private List<Transaction> sentTransactions;

	@OneToMany(mappedBy = "recipient")
	private List<Transaction> receivedTransactions;
}
