package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@OneToMany(mappedBy = "category")
	private List<Transaction> transactions;

	@OneToMany(mappedBy = "category")
	private List<Budget> budgets;
}
