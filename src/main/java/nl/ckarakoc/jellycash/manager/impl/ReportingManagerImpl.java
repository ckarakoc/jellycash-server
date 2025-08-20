package nl.ckarakoc.jellycash.manager.impl;

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

@Service
public class ReportingManagerImpl implements ReportingManager {
	final TransactionService transactionService;
	final BudgetService budgetService;
	final CategoryService categoryService;

	public ReportingManagerImpl(TransactionService transactionService, BudgetService budgetService, CategoryService categoryService) {
		this.transactionService = transactionService;
		this.budgetService = budgetService;
		this.categoryService = categoryService;
	}
}
