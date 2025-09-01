package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.RecurringBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringBillRepository extends JpaRepository<RecurringBill, Long> {

}
