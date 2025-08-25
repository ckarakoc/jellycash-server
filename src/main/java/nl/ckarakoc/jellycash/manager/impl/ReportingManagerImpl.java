package nl.ckarakoc.jellycash.manager.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.manager.ReportingManager;
import nl.ckarakoc.jellycash.service.BudgetService;
import nl.ckarakoc.jellycash.service.CategoryService;
import nl.ckarakoc.jellycash.service.TransactionService;
import org.springframework.stereotype.Service;

// todo: Combine data from multiple sources for reports
// - Use TransactionService for spending data
// - Use CategoryService for grouping
// - Use BudgetService for comparisons
// Generate monthly spending reports, trend analysis, etc.

@RequiredArgsConstructor
@Service
public class ReportingManagerImpl implements ReportingManager {
	private final TransactionService transactionService;
	private final BudgetService budgetService;
	private final CategoryService categoryService;
}
