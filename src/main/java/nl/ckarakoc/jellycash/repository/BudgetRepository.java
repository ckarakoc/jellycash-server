package nl.ckarakoc.jellycash.repository;

import java.util.List;
import java.util.Optional;
import nl.ckarakoc.jellycash.model.Budget;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

  List<Budget> findAllByUser(User user);

  Optional<Budget> findByBudgetIdAndUser(Long budgetId, User user);

  void deleteByBudgetIdAndUser(Long budgetId, User user);
}
