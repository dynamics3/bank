package ee.alex.bank.service;

import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ee.alex.bank.model.Account;
import ee.alex.bank.model.CheckingAccount;
import ee.alex.bank.model.SavingsAccount;
import static ee.alex.bank.model.AccountType.CHECKING;
import static ee.alex.bank.model.AccountType.SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * @author Aleksei Kulitškov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SavingsAccount.class)
public class BankServiceTest {

  @Test
  public void createSavingsAccount_CreatesSavingsAccountAndReturnsItsAccountNumber() throws Exception {
    // given
    BankService bankService = new BankService();

    // when
    UUID result = bankService.createSavingsAccount("Peter Parker");

    // then
    assertThat(result).isNotNull();

    Account createdAccount = bankService.findAccount(result);
    assertThat(createdAccount).isInstanceOfSatisfying(SavingsAccount.class, it -> {
      assertThat(it.getAccountId()).isEqualTo(result);
      assertThat(it.getOwner()).isEqualTo("Peter Parker");
      assertThat(it.getType()).isEqualTo(SAVINGS);
    });
  }

  @Test
  public void createCheckingAccount_CreatesCheckingAccountAndReturnsItsAccountNumber() throws Exception {
    // given
    BankService bankService = new BankService();

    // when
    UUID result = bankService.createCheckingAccount("Peter Parker", 500.0);

    // then
    assertThat(result).isNotNull();

    Account createdAccount = bankService.findAccount(result);
    assertThat(createdAccount).isInstanceOfSatisfying(CheckingAccount.class, it -> {
      assertThat(it.getAccountId()).isEqualTo(result);
      assertThat(it.getOwner()).isEqualTo("Peter Parker");
      assertThat(it.getType()).isEqualTo(CHECKING);
      assertThat(it.getLimit()).isEqualTo(500.0);
    });
  }

  @Test
  public void withdrawMoney_WithdrawsMoneyFromRightAccount_IfNoErrors() throws Exception {
    // given
    BankService bankService = spy(new BankService());
    Account account = spy(new CheckingAccount("Peter Parker", 500.0));
    UUID accountId = account.getAccountId();

    doReturn(account).when(bankService).findAccount(accountId);

    // when
    bankService.withdrawMoney(accountId, 200.0);

    // then
    assertThat(account.getBalance()).isEqualTo(-200.0);

    verify(account).withdraw(200.0);
  }

  @Test
  public void depositMoney_DepositsMoneyToRightAccount_IfNoErrors() throws Exception {
    // given
    BankService bankService = spy(new BankService());
    Account account = spy(new CheckingAccount("Peter Parker", 500.0));
    UUID accountId = account.getAccountId();

    doReturn(account).when(bankService).findAccount(accountId);

    // when
    bankService.depositMoney(accountId, 200.0);

    // then
    assertThat(account.getBalance()).isEqualTo(200.0);

    verify(account).deposit(200.0);
  }

  @Test
  public void getAccountDetails_RetrievesAccountDetails_IfNoErrors() throws Exception {
    // given
    BankService bankService = spy(new BankService());
    Account account = spy(new CheckingAccount("Peter Parker", 500.0));
    UUID accountId = account.getAccountId();

    doReturn(account).when(bankService).findAccount(accountId);

    // when
    String result = bankService.getAccountDetails(accountId);

    // then
    assertThat(result).isEqualTo(
        "Account details:\n" +
            "Holder: Peter Parker\n" +
            "Type: checking\n" +
            "Balance: 0.0\n" +
            "Limit: 500.0\n"
    );

    verify(account).getDetails();
  }

  @Test
  public void getAccountDetails_RetrievesAccountDetails_IfNoAccountFound() {
    // given
    BankService bankService = new BankService();
    UUID accountId = UUID.fromString("a2d10319-a240-4b29-bcd9-b2a546472747");

    // when
    String result = bankService.getAccountDetails(accountId);

    // then
    assertThat(result).isNull();
  }

  @Test
  public void changeCheckingAccountLimit_ChangesLimit_IfCheckingAccount() throws Exception {
    // given
    BankService bankService = spy(new BankService());
    CheckingAccount account = spy(new CheckingAccount("Peter Parker", 500.0));
    UUID accountId = account.getAccountId();

    doReturn(account).when(bankService).findAccount(accountId);

    // when
    bankService.changeCheckingAccountLimit(accountId, 200.0);

    // then
    verify(account).setLimit(200.0);
  }

  @Test
  public void transferMoney_TransfersMoneyToAnotherAccount_IfCheckingAccount() throws Exception {
    // given
    BankService bankService = spy(new BankService());
    CheckingAccount accountFrom = spy(new CheckingAccount("Peter Parker", 500.0));
    SavingsAccount accountTo = spy(new SavingsAccount("Rick Grimes"));
    UUID accountFromId = accountFrom.getAccountId();
    UUID accountToId = accountTo.getAccountId();

    doReturn(accountFrom).when(bankService).findAccount(accountFromId);
    doReturn(accountTo).when(bankService).findAccount(accountToId);

    // when
    bankService.transferMoney(accountFromId, accountToId, 200.0);

    // then
    assertThat(accountFrom.getBalance()).isEqualTo(-200.0);
    assertThat(accountTo.getBalance()).isEqualTo(200.0);

    verify(accountFrom).withdraw(200.0);
    verify(accountTo).deposit(200.0);
  }

  @Test
  public void provideInterestToAllUsers() throws Exception {
    // given
    BankService bankService = new BankService();

    UUID savingsAccount1Id = bankService.createSavingsAccount("Peter Parker");
    bankService.depositMoney(savingsAccount1Id, 100.0);
    UUID savingsAccount2Id = bankService.createSavingsAccount("Rick Grimes");
    bankService.depositMoney(savingsAccount2Id, 200.0);
    UUID savingsAccount3Id = bankService.createSavingsAccount("Nick Burns");
    bankService.depositMoney(savingsAccount3Id, 300.0);
    UUID checkingAccountId = bankService.createCheckingAccount("Mark Watson", 500.0);
    bankService.depositMoney(checkingAccountId, 400.0);

    Account savingsAccount1 = bankService.findAccount(savingsAccount1Id);
    Account savingsAccount2 = bankService.findAccount(savingsAccount2Id);
    Account savingsAccount3 = bankService.findAccount(savingsAccount3Id);
    Account checkingAccount = bankService.findAccount(checkingAccountId);

    // when
    bankService.provideInterestToAllUsers();

    // then
    assertThat(savingsAccount1.getBalance()).isEqualTo(120.0);
    assertThat(savingsAccount2.getBalance()).isEqualTo(240);
    assertThat(savingsAccount3.getBalance()).isEqualTo(360.0);
    assertThat(checkingAccount.getBalance()).isEqualTo(400.0);
  }

  @Test
  public void updateInterestRate_UpdatesInterestRate_IfNoErrors() throws Exception {
    // given
    BankService bankService = new BankService();

    mockStatic(SavingsAccount.class);

    // when
    bankService.updateInterestRate(0.3);

    // then
    verifyStatic(SavingsAccount.class);
    SavingsAccount.setInterestRate(0.3);
  }

}
