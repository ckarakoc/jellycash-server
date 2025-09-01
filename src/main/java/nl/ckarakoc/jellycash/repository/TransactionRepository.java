package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
