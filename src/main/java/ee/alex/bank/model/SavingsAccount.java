package ee.alex.bank.model;

import ee.alex.bank.exception.InsufficientFundsException;
import ee.alex.bank.exception.InvalidAmountException;
import ee.alex.bank.exception.InvalidInterestRateException;
import static ee.alex.bank.model.AccountType.SAVINGS;

/**
 * @author Aleksei Kulitškov
 */
public class SavingsAccount extends AbstractAccount {

  private static double interestRate = 0.2;

  public SavingsAccount(String owner) {
    super(owner);
  }

  public static double getInterestRate() {
    return interestRate;
  }

  public static void setInterestRate(double newInterestRate) throws InvalidInterestRateException {
    if (newInterestRate <= 0.0) {
      throw new InvalidInterestRateException();
    }

    interestRate = newInterestRate;
  }

  public AccountType getType() {
    return SAVINGS;
  }

  public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
    if (amount <= 0) {
      throw new InvalidAmountException();
    }

    if (this.getBalance() < amount) {
      throw new InsufficientFundsException();
    }

    this.setBalance(this.getBalance() - amount);
  }

  public void provideInterest() {
    double currentBalance = this.getBalance();
    this.setBalance(currentBalance + currentBalance * interestRate);
  }

  @Override
  protected String getAdditionalDetails() {
    return String.format("Interest rate: %s", interestRate);
  }

}
