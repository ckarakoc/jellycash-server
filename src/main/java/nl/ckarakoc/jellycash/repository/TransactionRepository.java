package nl.ckarakoc.jellycash.repository;

import java.util.List;
import java.util.Optional;
import nl.ckarakoc.jellycash.model.Transaction;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findAllBySender(User sender);

  Optional<Transaction> findByTransactionIdAndSender(Long id, User sender);
}
