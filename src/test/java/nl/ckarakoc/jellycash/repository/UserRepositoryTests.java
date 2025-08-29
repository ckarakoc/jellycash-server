package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
public class UserRepositoryTests {
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
	}

	@Nested
	class FindByEmailTests {

		@Test
		public void shouldReturnUser_whenUserExists() {
			User user = User.builder()
				.email("mark@rutte.nl")
				.password("passWord@123")
				.firstName("Mark")
				.lastName("Rutte")
				.build();

			entityManager.persistAndFlush(user);

			Optional<User> res = userRepository.findByEmail("mark@rutte.nl");

			assertThat(res).isPresent();
			assertThat(res.get().getEmail()).isEqualTo("mark@rutte.nl");
		}

		@Test
		public void shouldReturnEmpty_whenUserNotExists() {
			Optional<User> res = userRepository.findByEmail("mark@rutte.nl");

			assertThat(res).isNotPresent();
		}

	}

	@Nested
	class ExistsByEmailTests {
		@Test
		public void shouldReturnTrue_whenUserExists() {
			User user = User.builder()
				.email("mark@rutte.nl")
				.password("passWord@123")
				.firstName("Mark")
				.lastName("Rutte")
				.build();

			entityManager.persistAndFlush(user);

			boolean exists = userRepository.existsByEmail("mark@rutte.nl");
			assertThat(exists).isTrue();
		}

		@Test
		public void shouldReturnFalse_whenUserNotExists() {
			boolean exists = userRepository.existsByEmail("mark@rutte.nl");
			assertThat(exists).isFalse();
		}
	}
}
