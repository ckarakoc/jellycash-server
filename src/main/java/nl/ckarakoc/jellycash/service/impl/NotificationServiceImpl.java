package nl.ckarakoc.jellycash.service.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.service.BudgetService;
import nl.ckarakoc.jellycash.service.NotificationService;
import nl.ckarakoc.jellycash.service.PotService;
import nl.ckarakoc.jellycash.service.RecurringBillService;
import org.springframework.stereotype.Service;

// todo: Orchestrate alerts across the system
// - Call BudgetService to check if spending limits exceeded
// - Call RecurringBillService to find bills due soon
// - Call PotService to check if savings goals reached
// Send appropriate notifications based on all these checks

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

  private final BudgetService budgetService;
  private final RecurringBillService recurringBillService;
  private final PotService potService;
}
