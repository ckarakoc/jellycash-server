package nl.ckarakoc.jellycash.service.impl;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.transaction.CreateTransactionRequestDto;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.exception.UpdateEntityNotFoundException;
import nl.ckarakoc.jellycash.model.Transaction;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.TransactionRepository;
import nl.ckarakoc.jellycash.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  public TransactionServiceImpl(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override
  public List<Transaction> getAllTransactions(User user) {
    return transactionRepository.findAllBySender(user);
  }

  @Override
  public Transaction getTransaction(Long id, User user) {
    return transactionRepository.findByTransactionIdAndSender(id, user)
        .orElseThrow(() -> new UpdateEntityNotFoundException("Transaction with id " + id + " not found"));
  }

  @Override
  public Transaction createTransaction(CreateTransactionRequestDto dto, User user) {
    throw new NotImplementedException();
  }
}
