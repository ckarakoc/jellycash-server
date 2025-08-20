package nl.ckarakoc.jellycash.manager.impl;

import nl.ckarakoc.jellycash.manager.FinancialSummaryManager;
import nl.ckarakoc.jellycash.service.BudgetService;
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

@Service
public class FinancialSummaryManagerImpl implements FinancialSummaryManager {
	final TransactionService transactionService;
	final BudgetService budgetService;
	final PotService potService;
	final RecurringBillService recurringBillService;

	public FinancialSummaryManagerImpl(TransactionService transactionService, BudgetService budgetService, PotService potService, RecurringBillService recurringBillService) {
		this.transactionService = transactionService;
		this.budgetService = budgetService;
		this.potService = potService;
		this.recurringBillService = recurringBillService;
	}
}
