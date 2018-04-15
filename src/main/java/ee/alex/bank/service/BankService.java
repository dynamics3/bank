package ee.alex.bank.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import ee.alex.bank.exception.AccountErrorException;
import ee.alex.bank.exception.InvalidInterestRateException;
import ee.alex.bank.model.Account;
import ee.alex.bank.model.CheckingAccount;
import ee.alex.bank.model.SavingsAccount;
import static ee.alex.bank.model.AccountType.CHECKING;
import static ee.alex.bank.model.AccountType.SAVINGS;
import static java.lang.String.*;

/**
 * @author Aleksei Kulitškov
 */
public class BankService {

  private Set<Account> accounts = new HashSet<>();

  public UUID createSavingsAccount(String customer) {
    Account createdAccount = new SavingsAccount(customer);
    accounts.add(createdAccount);

    return createdAccount.getAccountId();
  }

  public UUID createCheckingAccount(String customer, double limit) {
    Account createdAccount = new CheckingAccount(customer, limit);
    accounts.add(createdAccount);

    return createdAccount.getAccountId();
  }

  public void withdrawMoney(UUID accountId, double amount) {
    try {
      findAccount(accountId).withdraw(amount);
      System.out.println(format("Withdrawn %s€ from bank account %S", amount, accountId));
    } catch (AccountErrorException e) {
      System.out.println(e.getMessage());
    }
  }

  public void depositMoney(UUID accountId, double amount) {
    try {
      findAccount(accountId).deposit(amount);
      System.out.println(format("Deposited %s€ to bank account %S", amount, accountId));
    } catch (AccountErrorException e) {
      System.out.println(e.getMessage());
    }
  }

  public String getAccountDetails(UUID accountId) {
    try {
      String details = findAccount(accountId).getDetails();
      System.out.println(details);
      return details;
    } catch (AccountErrorException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void changeCheckingAccountLimit(UUID accountId, double newLimit) {
    try {
      Account acc = findAccount(accountId);
      if (acc.getType() != CHECKING) {
        throw new AccountErrorException("Wrong account type");
      }

      ((CheckingAccount) acc).setLimit(newLimit);
      System.out.println(format("Changed limit to %s€ of bank account %s", newLimit, accountId));
    } catch (AccountErrorException e) {
      System.out.println(e.getMessage());
    }
  }

  public void transferMoney(UUID fromAccountId, UUID toAccountId, double amount) {
    try {
      Account accFrom = findAccount(fromAccountId);
      if (accFrom.getType() != CHECKING) {
        throw new AccountErrorException("Wrong account type");
      }

      Account accTo = findAccount(toAccountId);

      ((CheckingAccount) accFrom).transferMoney(accTo, amount);
      System.out.println(format("Transferred %s€ from account %s to account %s", amount, fromAccountId, toAccountId));
    } catch (AccountErrorException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateInterestRate(double interestRate) {
    try {
      double currentInterestRate = SavingsAccount.getInterestRate();
      SavingsAccount.setInterestRate(interestRate);
      System.out.println(format("Changed interest rate from %s to %s", currentInterestRate, interestRate));
    } catch (InvalidInterestRateException e) {
      System.out.println(e.getMessage());
    }
  }

  public void provideInterestToAllUsers() {
    System.out.println(format("Providing interest at rate %s", SavingsAccount.getInterestRate()));
    accounts.stream().filter(acc -> acc.getType() == SAVINGS).forEach(x -> ((SavingsAccount) x).provideInterest());
  }

  protected Account findAccount(UUID accountId) throws AccountErrorException {
    Optional<Account> accountOptional = accounts.stream().filter(acc -> acc.getAccountId() == accountId).findFirst();
    if (!accountOptional.isPresent()) {
      throw new AccountErrorException("Invalid account number");
    }

    return accountOptional.get();
  }

}
