package nl.ckarakoc.jellycash.service;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.budget.CreateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.PartialUpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.UpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.model.Budget;
import nl.ckarakoc.jellycash.model.User;

public interface BudgetService {

  List<Budget> getAllBudgets(User user);

  Budget getBudget(Long id, User user);

  Budget createBudget(CreateBudgetRequestDto dto, User user);

  Budget updateBudget(Long id, UpdateBudgetRequestDto dto, User user);

  Budget partialUpdateBudget(Long id, PartialUpdateBudgetRequestDto dto, User user);

  void deleteBudget(Long id, User user);

}
