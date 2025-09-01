package nl.ckarakoc.jellycash.stupid;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.BaseRepositoryTest;
import nl.ckarakoc.jellycash.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * Testing whether instance of BaseRepositoryTest is not shared. A postgresql container should be
 * started before all tests once, and stopped after all tests. The tables should be deleted after
 * every @Test method, while the DB persists between tests.
 *
 */
public class TestContainerTests extends BaseRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
      .withDatabaseName("jellycash");

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  User user;

  @Test
  public void test() {
    user = User.builder()
        .email("mark@rutte.nl")
        .password("passWord@123")
        .firstName("Mark")
        .lastName("Rutte")
        .build();
    entityManager.persistAndFlush(user);

    Optional<User> res = userRepository.findById(1L);
    assertThat(res).isPresent();
  }

  @Test
  public void nonPersistentTest() {
    Optional<User> res = userRepository.findById(1L);
    assertThat(res).isNotPresent();
  }

}
