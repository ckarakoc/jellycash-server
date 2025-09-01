package nl.ckarakoc.jellycash.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class RoleRepositoryTests extends BaseRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
      .withDatabaseName("jellycash");

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RoleRepository roleRepository;

  @Nested
  class FindByRoleTests {

    @Test
    public void shouldReturnRole_whenRoleExists() {
      Role role = Role.builder()
          .role(AppRole.ADMIN)
          .build();

      entityManager.persistAndFlush(role);

      Optional<Role> res = roleRepository.findByRole(AppRole.ADMIN);

      assertThat(res).isPresent();
      assertThat(res.get().getRole()).isEqualTo(AppRole.ADMIN);
    }

    @Test
    public void shouldReturnEmpty_whenRoleDoesNotExists() {
      Role role = Role.builder()
          .role(AppRole.USER)
          .build();
      entityManager.persistAndFlush(role);
      Optional<Role> res = roleRepository.findByRole(AppRole.ADMIN);

      assertThat(res).isNotPresent();
    }

  }

  @Nested
  class ExistsByRoleTests {

    @Test
    public void shouldReturnTrue_whenRoleExists() {
      Role role = Role.builder()
          .role(AppRole.ADMIN)
          .build();

      entityManager.persistAndFlush(role);

      boolean exists = roleRepository.existsByRole(AppRole.ADMIN);
      assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalse_whenRoleDoesNotExists() {
      Role role = Role.builder()
          .role(AppRole.ADMIN)
          .build();

      entityManager.persistAndFlush(role);

      boolean exists = roleRepository.existsByRole(AppRole.USER);
      assertThat(exists).isFalse();
    }

  }
}
