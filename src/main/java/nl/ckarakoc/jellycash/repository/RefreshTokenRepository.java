package nl.ckarakoc.jellycash.repository;

import java.util.Optional;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByUser(User user);

  Optional<RefreshToken> findByToken(String token);

  void deleteByUser(User user);

  void deleteByToken(String token);

  boolean existsByToken(String token);

  boolean existsByTokenAndUser(String token, User user);
}
