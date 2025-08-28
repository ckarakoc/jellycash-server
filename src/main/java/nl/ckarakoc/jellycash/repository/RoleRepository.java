package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	boolean existsByRole(AppRole role);

	Optional<Role> findByRole(AppRole role);
}
