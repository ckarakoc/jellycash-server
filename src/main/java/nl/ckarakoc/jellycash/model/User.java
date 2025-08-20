package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;

	@Column
	private String firstName;
	private String lastName;
	private String avatar = ""; // todo: location to default avatar image
	private String currency = "EUR";
	private Long balance = 0L;
	private Long income = 0L;
	private Long expenses = 0L;
	private Boolean isAdmin = false;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "user")
	@ToString.Exclude
	private List<Pot> pots;

	@OneToMany(mappedBy = "user")
	@ToString.Exclude
	private List<Budget> budgets;

	@OneToMany(mappedBy = "sender")
	@ToString.Exclude
	private List<Transaction> sentTransactions;

	@OneToMany(mappedBy = "recipient")
	@ToString.Exclude
	private List<Transaction> receivedTransactions;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User user)) return false;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}
}
