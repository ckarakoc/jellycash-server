package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "budgets")
public class Budget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "budget_id", nullable = false)
	private Long budgetId;

	private Long amount;
	private Long targetAmount;
	private Boolean isDone;
	private LocalDateTime startDate;
	private LocalDateTime dueDate;
	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Budget budget)) return false;
		return Objects.equals(budgetId, budget.budgetId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(budgetId);
	}
}
