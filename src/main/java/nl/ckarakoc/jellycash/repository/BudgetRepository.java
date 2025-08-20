package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
