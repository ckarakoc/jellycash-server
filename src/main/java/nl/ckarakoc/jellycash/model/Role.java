package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.ckarakoc.jellycash.model.enums.AppRole;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppRole role;

	@ManyToMany(mappedBy = "roles")
	@ToString.Exclude
	private Set<User> users;
}
