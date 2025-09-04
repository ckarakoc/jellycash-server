package nl.ckarakoc.jellycash.repository;

import java.util.List;
import java.util.Optional;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotRepository extends JpaRepository<Pot, Long> {

  List<Pot> findAllByUser(User user);

  Optional<Pot> findByUserAndPotId(User user, Long potId);

  void deleteByPotIdAndUser(Long potId, User user);
}
