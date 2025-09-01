package nl.ckarakoc.jellycash.repository;

import java.util.Optional;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  boolean existsByRole(AppRole role);

  Optional<Role> findByRole(AppRole role);
}
