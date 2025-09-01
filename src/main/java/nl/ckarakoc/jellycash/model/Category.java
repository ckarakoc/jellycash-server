package nl.ckarakoc.jellycash.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  // TODO: should removing a category remove all budgets?
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  // @OneToMany(mappedBy = "category")
  @ToString.Exclude
  private List<Budget> budgets;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Category category)) {
      return false;
    }
    return Objects.equals(categoryId, category.categoryId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(categoryId);
  }
}
