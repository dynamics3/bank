package ee.alex.bank.model;

import org.junit.Test;
import ee.alex.bank.exception.InvalidAmountException;
import ee.alex.bank.exception.LimitReachedException;
import static ee.alex.bank.model.AccountType.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Aleksei Kulitškov
 */
public class CheckingAccountTest {

  @Test
  public void setLimit_UpdatesLimit_IfNewLimitIsPositive() throws Exception {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    account.setLimit(600.0);

    // then
    assertThat(account.getLimit()).isEqualTo(600.0);
  }

  @Test
  public void setLimit_ThrowsInvalidAmountException_IfNewLimitIsZero() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.setLimit(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void setLimit_ThrowsInvalidAmountException_IfNewLimitIsNegative() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.setLimit(-50.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void getType_ReturnsChecking() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    AccountType result = account.getType();

    // then
    assertThat(result).isEqualTo(CHECKING);
  }

  @Test
  public void withdraw_WithdrawsSpecifiedAmount_IfAmountIsPositive() throws Exception {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);
    account.setBalance(100.0);

    // when
    account.withdraw(50.0);

    // then
    assertThat(account.getBalance()).isEqualTo(50.0);
  }

  @Test
  public void withdraw_WithdrawsSpecifiedAmount_IfAmountIsPositiveAndGoesBelowZero() throws Exception {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    account.withdraw(100.0);

    // then
    assertThat(account.getBalance()).isEqualTo(-100.0);
  }

  @Test
  public void withdraw_ThrowsInvalidAmountException_IfAmountIsZero() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.withdraw(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void withdraw_ThrowsInvalidAmountException_IfAmountIsNegative() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.withdraw(-50.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void withdraw_ThrowsLimitReachedException_IfBalanceGoesBeyondLimit() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.withdraw(600.0));

    // then
    assertThat(result).isInstanceOf(LimitReachedException.class).hasMessage("Limit reached");
  }

  @Test
  public void deposit_DepositsSpecifiedAmount_IfAmountIsPositive() throws Exception {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);
    assertThat(account.getBalance()).isEqualTo(0.0);

    // when
    account.deposit(50.0);

    // then
    assertThat(account.getBalance()).isEqualTo(50.0);
  }

  @Test
  public void deposit_ThrowsInvalidAmountException_IfAmountIsZero() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.deposit(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void deposit_ThrowsInvalidAmountException_IfAmountIsNegative() {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);

    // when
    Throwable result = catchThrowable(() -> account.deposit(-50.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void transferMoney_AccountFromBalanceReducesAndAccountToBalanceIncreasesBySpecifiedAmountOfMoney_IfTransferCompletesSuccessfully() throws Exception {
    // given
    CheckingAccount accountFrom = new CheckingAccount("Peter Parker", 500.0);
    Account accountTo = new SavingsAccount("Ave Ott");

    // when
    accountFrom.transferMoney(accountTo, 100.0);

    // then
    assertThat(accountFrom.getBalance()).isEqualTo(-100.0);
    assertThat(accountTo.getBalance()).isEqualTo(100.0);
  }

  @Test
  public void transferMoney_ThrowsInvalidAmountException_IfAmountIsZero() {
    // given
    CheckingAccount accountFrom = new CheckingAccount("Peter Parker", 500.0);
    Account accountTo = new SavingsAccount("Ave Ott");

    // when
    Throwable result = catchThrowable(() -> accountFrom.transferMoney(accountTo, 0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void transferMoney_ThrowsInvalidAmountException_IfAmountIsNegative() {
    // given
    CheckingAccount accountFrom = new CheckingAccount("Peter Parker", 500.0);
    Account accountTo = new SavingsAccount("Ave Ott");

    // when
    Throwable result = catchThrowable(() -> accountFrom.transferMoney(accountTo, -100.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void transferMoney_ThrowsLimitReachedException_IfBalanceGoesBeyondLimit() {
    // given
    CheckingAccount accountFrom = new CheckingAccount("Peter Parker", 500.0);
    Account accountTo = new SavingsAccount("Ave Ott");

    // when
    Throwable result = catchThrowable(() -> accountFrom.transferMoney(accountTo, 600.0));

    // then
    assertThat(result).isInstanceOf(LimitReachedException.class).hasMessage("Limit reached");
  }

  @Test
  public void getDetails_CorrectInformationAboutAccount() throws Exception {
    // given
    CheckingAccount account = new CheckingAccount("Peter Parker", 500.0);
    account.deposit(150.0);

    // when
    String result = account.getDetails();

    // then
    assertThat(result).isEqualTo(
        "Account details:\n" +
            "Holder: Peter Parker\n" +
            "Type: checking\n" +
            "Balance: 150.0\n" +
            "Limit: 500.0\n"
    );
  }

}
