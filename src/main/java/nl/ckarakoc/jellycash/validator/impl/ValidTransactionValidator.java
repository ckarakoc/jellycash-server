package nl.ckarakoc.jellycash.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.ckarakoc.jellycash.model.Transaction;
import nl.ckarakoc.jellycash.validator.ValidTransaction;

public class ValidTransactionValidator implements ConstraintValidator<ValidTransaction, Transaction> {

  @Override
  public boolean isValid(Transaction value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    boolean hasExternalParty = value.getExternalParty() != null && !value.getExternalParty().isBlank();
    boolean hasSender = value.getSender() != null;
    boolean hasRecipient = value.getRecipient() != null;

    int count = (hasExternalParty ? 1 : 0) + (hasSender ? 1 : 0) + (hasRecipient ? 1 : 0);

    return count == 2;
  }
}
