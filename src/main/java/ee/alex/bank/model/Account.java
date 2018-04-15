package ee.alex.bank.model;

import java.util.UUID;
import ee.alex.bank.exception.AccountErrorException;
import ee.alex.bank.exception.InvalidAmountException;

/**
 * @author Aleksei Kulitškov
 */
public interface Account {

  void deposit(double amount) throws InvalidAmountException;

  void withdraw(double amount) throws AccountErrorException;

  String getDetails();

  UUID getAccountId();

  AccountType getType();

  double getBalance();

  String getOwner();

}
