package ee.alex.bank.model;

import ee.alex.bank.exception.InvalidAmountException;
import ee.alex.bank.exception.LimitReachedException;
import static ee.alex.bank.model.AccountType.CHECKING;

/**
 * @author Aleksei Kulitškov
 */
public class CheckingAccount extends AbstractAccount {

  private double limit;

  public CheckingAccount(String owner, double limit) {
    super(owner);
    this.limit = limit;
  }

  public double getLimit() {
    return limit;
  }

  public void setLimit(double limit) throws InvalidAmountException {
    if (limit <= 0) {
      throw new InvalidAmountException();
    }
    this.limit = limit;
  }

  public AccountType getType() {
    return CHECKING;
  }

  public void withdraw(double amount) throws InvalidAmountException, LimitReachedException {
    if (amount <= 0.0) {
      throw new InvalidAmountException();
    }

    double currentBalance = this.getBalance();
    if (Math.abs(currentBalance - amount) > limit) {
      throw new LimitReachedException();
    }

    this.setBalance(currentBalance - amount);
  }

  public void transferMoney(Account to, double amount) throws InvalidAmountException, LimitReachedException {
    this.withdraw(amount);
    to.deposit(amount);
  }

  @Override
  protected String getAdditionalDetails() {
    return String.format("Limit: %s", this.limit);
  }

}
