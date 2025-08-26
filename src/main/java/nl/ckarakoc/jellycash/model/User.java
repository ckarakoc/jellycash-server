package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
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
	private String avatar = ""; // todo: location to default avatar image
	@Column(length = 3)
	private String currency = "EUR";
	private Long balance = 0L;
	private Long income = 0L;
	private Long expenses = 0L;

	//region security
	@Column(nullable = false)
	private boolean enabled = true;

	@Column(nullable = false)
	private boolean accountNonExpired = true;

	@Column(nullable = false)
	private boolean accountNonLocked = true;

	@Column(nullable = false)
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
	//endregion

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	@ToString.Exclude
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

	@OneToMany(mappedBy = "sender", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@ToString.Exclude
	private List<Transaction> sentTransactions;

	@OneToMany(mappedBy = "recipient", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
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
