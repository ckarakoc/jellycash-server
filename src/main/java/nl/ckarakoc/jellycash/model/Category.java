package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Column(nullable = false, unique = true)
	private String name;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "category")
	@ToString.Exclude
	private List<Transaction> transactions;

	// todo: should removing a category remove all budgets?
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	//@OneToMany(mappedBy = "category")
	@ToString.Exclude
	private List<Budget> budgets;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Category category)) return false;
		return Objects.equals(categoryId, category.categoryId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(categoryId);
	}
}
