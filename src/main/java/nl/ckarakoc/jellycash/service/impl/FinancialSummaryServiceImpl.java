package nl.ckarakoc.jellycash.service.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.service.BudgetService;
import nl.ckarakoc.jellycash.service.FinancialSummaryService;
import nl.ckarakoc.jellycash.service.PotService;
import nl.ckarakoc.jellycash.service.RecurringBillService;
import nl.ckarakoc.jellycash.service.TransactionService;
import org.springframework.stereotype.Service;

// todo: Coordinates multiple services to build dashboard data
// - Use TransactionService to get recent transactions
// - Use BudgetService to check spending vs budgets
// - Use PotService to get savings progress
// - Use RecurringBillService for upcoming bills
// Return a composite DTO with all dashboard info

@RequiredArgsConstructor
@Service
public class FinancialSummaryServiceImpl implements FinancialSummaryService {

  private final TransactionService transactionService;
  private final BudgetService budgetService;
  private final PotService potService;
  private final RecurringBillService recurringBillService;
}
