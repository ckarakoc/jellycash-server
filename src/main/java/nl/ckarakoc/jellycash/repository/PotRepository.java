package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.Pot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotRepository extends JpaRepository<Pot, Long> {
}
