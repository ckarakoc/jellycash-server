package nl.ckarakoc.jellycash.service.impl;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.budget.CreateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.PartialUpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.budget.UpdateBudgetRequestDto;
import nl.ckarakoc.jellycash.exception.UpdateEntityNotFoundException;
import nl.ckarakoc.jellycash.model.Budget;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.BudgetRepository;
import nl.ckarakoc.jellycash.service.BudgetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetServiceImpl implements BudgetService {

  private final ModelMapper modelMapper;
  private final ModelMapper skipNullMapper;
  private final BudgetRepository budgetRepository;

  public BudgetServiceImpl(ModelMapper modelMapper, @Qualifier("skipNullMapper") ModelMapper skipNullMapper, BudgetRepository budgetRepository) {
    this.modelMapper = modelMapper;
    this.skipNullMapper = skipNullMapper;
    this.budgetRepository = budgetRepository;
  }

  @Override
  public List<Budget> getAllBudgets(User user) {
    return budgetRepository.findAllByUser(user);
  }

  @Override
  public Budget getBudget(Long id, User user) {
    Budget budget = budgetRepository.findByBudgetIdAndUser(id, user)
        .orElseThrow(() -> new UpdateEntityNotFoundException("Budget with id " + id + " not found"));
    return budget;
  }

  @Transactional
  @Override
  public Budget createBudget(CreateBudgetRequestDto dto, User user) {
    Budget created = modelMapper.map(dto, Budget.class);
    created.setUser(user);
    created.setIsDone(created.getAmount().compareTo(created.getTargetAmount()) >= 0);
    // TODO: created.setCategory(...)
    return budgetRepository.save(created);
  }

  @Transactional
  @Modifying
  @Override
  public Budget updateBudget(Long id, UpdateBudgetRequestDto dto, User user) {
    Budget updated = getBudget(id, user);
    modelMapper.map(dto, updated);
    return budgetRepository.save(updated);
  }

  @Transactional
  @Modifying
  @Override
  public Budget partialUpdateBudget(Long id, PartialUpdateBudgetRequestDto dto, User user) {
    Budget updated = getBudget(id, user);
    skipNullMapper.map(dto, updated);
    return budgetRepository.save(updated);
  }

  @Transactional
  @Override
  public void deleteBudget(Long id, User user) {
    budgetRepository.deleteByBudgetIdAndUser(id, user);
  }
}
