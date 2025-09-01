package nl.ckarakoc.jellycash.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class RefreshTokenRepositoryTests extends BaseRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
      .withDatabaseName("jellycash");

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RefreshTokenRepository tokenRepository;

  private User user;
  private RefreshToken token;

  @BeforeEach
  public void setUp() {
    token = RefreshToken.builder()
        .token("tiktok")
        .expiryDate(new Date(System.currentTimeMillis()))
        .build();

    user = User.builder()
        .email("mark@rutte.nl")
        .password("passWord@123")
        .firstName("Mark")
        .lastName("Rutte")
        .build();
  }

  @Nested
  class FindByUserTests {

    @Test
    public void shouldReturnToken_whenUserExists() {
      entityManager.persistAndFlush(user);

      token.setUser(user);
      entityManager.persistAndFlush(token);

      Optional<RefreshToken> res = tokenRepository.findByUser(user);

      assertThat(res).isPresent();
      assertThat(res.get().getToken()).isEqualTo("tiktok");
      assertThat(res.get().getUser()).isEqualTo(user);
    }

    @Test
    public void shouldReturnEmpty_whenUserDoesNotExists() {
      entityManager.persistAndFlush(user);
      entityManager.persistAndFlush(token);

      Optional<RefreshToken> res = tokenRepository.findByUser(user);

      assertThat(res).isNotPresent();
      assertThat(token.getUser()).isNull();
    }
  }

  @Nested
  class FindByTokenTests {

    @Test
    public void shouldReturnToken_whenTokenExists() {
      entityManager.persistAndFlush(token);
      Optional<RefreshToken> res = tokenRepository.findByToken("tiktok");

      assertThat(res).isPresent();
      assertThat(res.get().getToken()).isEqualTo(token.getToken());
    }

    @Test
    public void shouldReturnEmpty_whenTokenDoesNotExists() {
      entityManager.persistAndFlush(token);
      Optional<RefreshToken> res = tokenRepository.findByToken(token.getToken() + "123");

      assertThat(res).isNotPresent();
      assertThat(tokenRepository.findAll()).hasSize(1);
    }
  }

  @Nested
  class DeleteByUserTests {

    @Test
    public void shouldDeleteToken_whenUserExists() {
      entityManager.persistAndFlush(user);
      token.setUser(user);
      entityManager.persistAndFlush(token);
      tokenRepository.deleteByUser(user);

      assertThat(user.getRefreshToken()).isNull();
      assertThat(tokenRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldDoNothing_whenUserDoesNotExists() {
      User nonExistentUser = User.builder()
          .userId(999L)
          .email("donald@trump.com")
          .password("MakeAmericaGreat@45")
          .firstName("Donald")
          .lastName("Trump")
          .build();

      entityManager.persistAndFlush(token);
      assertThat(tokenRepository.findAll()).hasSize(1);

      tokenRepository.deleteByUser(nonExistentUser);
      assertThat(tokenRepository.findAll()).hasSize(1);
    }
  }

  @Nested
  class DeleteByTokenTests {

    @Test
    public void shouldDeleteToken_whenTokenExists() {
      entityManager.persistAndFlush(token);
      assertThat(tokenRepository.findAll()).hasSize(1);

      tokenRepository.deleteByToken(token.getToken());
      assertThat(tokenRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldDoNothing_whenTokenDoesNotExists() {
      entityManager.persistAndFlush(token);
      assertThat(tokenRepository.findAll()).hasSize(1);

      tokenRepository.deleteByToken(token.getToken() + "123");
      assertThat(tokenRepository.findAll()).hasSize(1);
    }
  }

  @Nested
  class ExistsByTokenTests {

    @Test
    public void shouldReturnTrue_whenTokenExists() {
      entityManager.persistAndFlush(token);
      boolean exists = tokenRepository.existsByToken(token.getToken());
      assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalse_whenTokenDoesNotExists() {
      entityManager.persistAndFlush(token);
      assertThat(tokenRepository.findAll()).hasSize(1);

      boolean exists = tokenRepository.existsByToken(token.getToken() + "123");
      assertThat(tokenRepository.findAll()).hasSize(1);
      assertThat(exists).isFalse();
    }
  }

  @Nested
  class ExistsByTokenAndUserTests {

    @Test
    public void shouldReturnTrue_whenTokenAndUserExists() {
      entityManager.persistAndFlush(user);
      token.setUser(user);
      entityManager.persistAndFlush(token);

      boolean exists = tokenRepository.existsByTokenAndUser(token.getToken(), user);
      assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalse_whenTokenAndUserDoesNotExists() {
      entityManager.persistAndFlush(user);
      token.setUser(user);
      entityManager.persistAndFlush(token);

      User nonExistentUser = User.builder()
          .userId(999L)
          .email("donald@trump.com")
          .password("MakeAmericaGreat@45")
          .firstName("Donald")
          .lastName("Trump")
          .build();

      boolean exists = tokenRepository.existsByTokenAndUser(token.getToken() + "123",
          nonExistentUser);
      assertThat(exists).isFalse();
    }

    @Test
    public void shouldReturnFalse_whenTokenXorUserExists() {
      entityManager.persistAndFlush(user);
      token.setUser(user);
      entityManager.persistAndFlush(token);

      User nonExistentUser = User.builder()
          .userId(999L)
          .email("donald@trump.com")
          .password("MakeAmericaGreat@45")
          .firstName("Donald")
          .lastName("Trump")
          .build();

      RefreshToken nonExistentToken = RefreshToken.builder()
          .refreshTokenId(999L)
          .token(token.getToken() + "123")
          .expiryDate(new Date(System.currentTimeMillis()))
          .build();

      boolean exists = tokenRepository.existsByTokenAndUser(nonExistentToken.getToken(), user);
      assertThat(exists).isFalse();

      exists = tokenRepository.existsByTokenAndUser(token.getToken(), nonExistentUser);
      assertThat(exists).isFalse();
    }
  }
}
