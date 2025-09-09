package nl.ckarakoc.jellycash.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  private Long userId;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  private String firstName;
  private String lastName;
  @Builder.Default
  private String avatar = ""; // TODO: location to default avatar image
  @Column(length = 3)
  @Builder.Default
  private String currency = "EUR";
  @Builder.Default
  private BigDecimal balance = BigDecimal.ZERO;
  @Builder.Default
  private BigDecimal income = BigDecimal.ZERO;
  @Builder.Default
  private BigDecimal expenses = BigDecimal.ZERO;

  // region security
  @Column(nullable = false)
  @Builder.Default
  private boolean enabled = true;

  @Column(nullable = false)
  @Builder.Default
  private boolean accountNonExpired = true;

  @Column(nullable = false)
  @Builder.Default
  private boolean accountNonLocked = true;

  @Column(nullable = false)
  @Builder.Default
  private boolean credentialsNonExpired = true;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private RefreshToken refreshToken;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream()
        .map(a -> new SimpleGrantedAuthority("ROLE_" + a.getRole().name()))
        .toList();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
  // endregion

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @ToString.Exclude
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Pot> pots;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Budget> budgets;

  @OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @ToString.Exclude
  private List<Transaction> sentTransactions;

  @OneToMany(mappedBy = "recipient", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @ToString.Exclude
  private List<Transaction> receivedTransactions;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Category> categories;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof User user)) {
      return false;
    }
    return Objects.equals(userId, user.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userId);
  }
}
