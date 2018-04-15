package ee.alex.bank;

import java.util.UUID;
import ee.alex.bank.service.BankService;

/**
 * @author Aleksei Kulitškov
 */
public class Bank {

  public static void main(String[] args) {
    BankService bankService = new BankService();

    UUID savingsAccountNumber1 = bankService.createSavingsAccount("Peter Parker");
    UUID savingsAccountNumber2 = bankService.createSavingsAccount("Rick Grimes");

    UUID checkingAccountNumber1 = bankService.createCheckingAccount("Peter Parker", 500.0);
    UUID checkingAccountNumber2 = bankService.createCheckingAccount("Rick Grimes", 120.0);

    // Testing checking accounts
    System.out.println("=== CHECKING ACCOUNTS ===");
    bankService.depositMoney(checkingAccountNumber1, 100.0);
    bankService.getAccountDetails(checkingAccountNumber1);

    bankService.withdrawMoney(checkingAccountNumber1, 100.0);
    bankService.getAccountDetails(checkingAccountNumber1);

    bankService.transferMoney(checkingAccountNumber1, savingsAccountNumber2, 100.0);
    bankService.getAccountDetails(checkingAccountNumber1);
    bankService.getAccountDetails(savingsAccountNumber2);

    bankService.changeCheckingAccountLimit(checkingAccountNumber2, 200.0);
    bankService.getAccountDetails(checkingAccountNumber2);

    // Errors
    bankService.withdrawMoney(checkingAccountNumber1, 1000.0);
    bankService.withdrawMoney(checkingAccountNumber1, 0.0);
    bankService.withdrawMoney(checkingAccountNumber1, -1000.0);

    bankService.depositMoney(checkingAccountNumber1, 0.0);
    bankService.depositMoney(checkingAccountNumber1, -1000.0);

    bankService.transferMoney(savingsAccountNumber2, savingsAccountNumber1, 1000.0);

    // Testing savings accounts
    System.out.println("=== SAVINGS ACCOUNTS ===");
    bankService.depositMoney(savingsAccountNumber1, 200.0);
    bankService.getAccountDetails(savingsAccountNumber1);

    bankService.withdrawMoney(savingsAccountNumber1, 100.0);
    bankService.getAccountDetails(savingsAccountNumber1);

    bankService.provideInterestToAllUsers();
    bankService.getAccountDetails(savingsAccountNumber1);
    bankService.getAccountDetails(savingsAccountNumber2);

    bankService.updateInterestRate(0.3);
    bankService.provideInterestToAllUsers();
    bankService.getAccountDetails(savingsAccountNumber1);
    bankService.getAccountDetails(savingsAccountNumber2);

    // Errors
    bankService.withdrawMoney(savingsAccountNumber2, 1000.0);
    bankService.withdrawMoney(savingsAccountNumber2, 0.0);
    bankService.withdrawMoney(savingsAccountNumber2, -1000.0);

    bankService.depositMoney(savingsAccountNumber2, 0.0);
    bankService.depositMoney(savingsAccountNumber2, -1000.0);
  }

}
