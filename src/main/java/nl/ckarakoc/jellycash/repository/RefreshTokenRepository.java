package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUser(User user);

	Optional<RefreshToken> findByToken(String token);

	@Modifying
	@Transactional
	void deleteByUser(User user);

	@Modifying
	@Transactional
	void deleteByToken(String token);

	boolean existsByToken(String token);

	boolean existsByTokenAndUser(String token, User user);
}
