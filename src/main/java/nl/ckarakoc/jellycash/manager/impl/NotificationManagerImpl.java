package nl.ckarakoc.jellycash.manager.impl;

import nl.ckarakoc.jellycash.manager.NotificationManager;
import nl.ckarakoc.jellycash.service.BudgetService;
import nl.ckarakoc.jellycash.service.PotService;
import nl.ckarakoc.jellycash.service.RecurringBillService;
import org.springframework.stereotype.Service;

// todo: Orchestrate alerts across the system
// - Call BudgetService to check if spending limits exceeded
// - Call RecurringBillService to find bills due soon
// - Call PotService to check if savings goals reached
// Send appropriate notifications based on all these checks

@Service
public class NotificationManagerImpl implements NotificationManager {
	final BudgetService budgetService;
	final RecurringBillService recurringBillService;
	final PotService potService;

	public NotificationManagerImpl(BudgetService budgetService, RecurringBillService recurringBillService, PotService potService) {
		this.budgetService = budgetService;
		this.recurringBillService = recurringBillService;
		this.potService = potService;
	}
}
