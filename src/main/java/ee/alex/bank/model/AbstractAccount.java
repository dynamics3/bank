package ee.alex.bank.model;

import java.util.UUID;
import ee.alex.bank.exception.AccountErrorException;
import ee.alex.bank.exception.InvalidAmountException;

/**
 * @author Aleksei Kulitškov
 */
abstract class AbstractAccount implements Account {

  private UUID accountId;
  private String owner;
  private double balance;

  AbstractAccount(String owner) {
    this.accountId = UUID.randomUUID();
    this.owner = owner;
    this.balance = 0.0;
  }

  public UUID getAccountId() {
    return accountId;
  }

  public double getBalance() {
    return balance;
  }

  public String getOwner() {
    return owner;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public abstract AccountType getType();

  public abstract void withdraw(double amount) throws AccountErrorException;

  public void deposit(double amount) throws InvalidAmountException {
    if (amount <= 0) {
      throw new InvalidAmountException();
    }

    this.balance += amount;
  }

  abstract String getAdditionalDetails();

  public String getDetails() {
    return this.toString();
  }

  @Override
  public String toString() {
    String ACCOUNT_DETAILS_MESSAGE = "Account details:\nHolder: %s\nType: %s\nBalance: %s\n";
    String mainMsg = String.format(ACCOUNT_DETAILS_MESSAGE, this.owner, this.getType().name().toLowerCase(), this.balance);

    return mainMsg + getAdditionalDetails() + "\n";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbstractAccount that = (AbstractAccount) o;

    return getAccountId().equals(that.getAccountId());
  }

  @Override
  public int hashCode() {
    return getAccountId().hashCode();
  }
}
