package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "budgets")
public class Budget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "budget_id", nullable = false)
	private Long budgetId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
}
