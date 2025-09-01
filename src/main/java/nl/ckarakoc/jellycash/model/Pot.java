package nl.ckarakoc.jellycash.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
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

  @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<PotTransaction> potTransactions;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pot pot)) {
      return false;
    }
    return Objects.equals(potId, pot.potId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(potId);
  }
}
