package nl.ckarakoc.jellycash.service.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.service.BudgetService;
import nl.ckarakoc.jellycash.service.CategoryService;
import nl.ckarakoc.jellycash.service.ReportingService;
import nl.ckarakoc.jellycash.service.TransactionService;
import org.springframework.stereotype.Service;

// TODO: Combine data from multiple sources for reports
// - Use TransactionService for spending data
// - Use CategoryService for grouping
// - Use BudgetService for comparisons
// Generate monthly spending reports, trend analysis, etc.

@RequiredArgsConstructor
@Service
public class ReportingServiceImpl implements ReportingService {

  private final TransactionService transactionService;
  private final BudgetService budgetService;
  private final CategoryService categoryService;
}
