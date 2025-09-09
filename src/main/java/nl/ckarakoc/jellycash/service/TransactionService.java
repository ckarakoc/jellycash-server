package nl.ckarakoc.jellycash.service;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.transaction.CreateTransactionRequestDto;
import nl.ckarakoc.jellycash.model.Transaction;
import nl.ckarakoc.jellycash.model.User;

public interface TransactionService {

  List<Transaction> getAllTransactions(User user);

  Transaction getTransaction(Long id, User user);

  Transaction createTransaction(CreateTransactionRequestDto dto, User user);
}
