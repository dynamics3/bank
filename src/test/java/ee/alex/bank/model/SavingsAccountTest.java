package ee.alex.bank.model;

import org.junit.Test;
import ee.alex.bank.exception.InsufficientFundsException;
import ee.alex.bank.exception.InvalidAmountException;
import ee.alex.bank.exception.InvalidInterestRateException;
import static ee.alex.bank.model.AccountType.SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Aleksei Kulitškov
 */
public class SavingsAccountTest {

  @Test
  public void setInterestRate_UpdatesInterestRate_IfNewInterestRateIsPositive() throws Exception {
    // given
    assertThat(SavingsAccount.getInterestRate()).isEqualTo(0.2);

    // when
    SavingsAccount.setInterestRate(0.5);

    // then
    assertThat(SavingsAccount.getInterestRate()).isEqualTo(0.5);
    // return to default value
    SavingsAccount.setInterestRate(0.2);
  }

  @Test
  public void setInterestRate_ThrowsInvalidInterestRateException_IfNewInterestRateIsZero() {
    // given
    assertThat(SavingsAccount.getInterestRate()).isEqualTo(0.2);

    // when
    Throwable result = catchThrowable(() -> SavingsAccount.setInterestRate(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidInterestRateException.class).hasMessage("Interest rate must be greater than zero");
  }

  @Test
  public void setInterestRate_ThrowsInvalidInterestRateException_IfNewInterestRateIsNegative() {
    // given
    assertThat(SavingsAccount.getInterestRate()).isEqualTo(0.2);

    // when
    Throwable result = catchThrowable(() -> SavingsAccount.setInterestRate(-0.5));

    // then
    assertThat(result).isInstanceOf(InvalidInterestRateException.class).hasMessage("Interest rate must be greater than zero");
  }

  @Test
  public void getType_ReturnsSavings() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    AccountType result = account.getType();

    // then
    assertThat(result).isEqualTo(SAVINGS);
  }

  @Test
  public void withdraw_WithdrawsSpecifiedAmount_IfAmountIsPositive() throws Exception {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");
    account.setBalance(100.0);

    // when
    account.withdraw(50.0);

    // then
    assertThat(account.getBalance()).isEqualTo(50.0);
  }

  @Test
  public void withdraw_ThrowsInvalidAmountException_IfAmountIsZero() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    Throwable result = catchThrowable(() -> account.withdraw(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void withdraw_ThrowsInvalidAmountException_IfAmountIsNegative() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    Throwable result = catchThrowable(() -> account.withdraw(-50.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void withdraw_ThrowsInsufficientFundsException_IfBalanceGoesBeyondZero() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    Throwable result = catchThrowable(() -> account.withdraw(600.0));

    // then
    assertThat(result).isInstanceOf(InsufficientFundsException.class).hasMessage("Insufficient funds");
  }

  @Test
  public void deposit_DepositsSpecifiedAmount_IfAmountIsPositive() throws Exception {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");
    assertThat(account.getBalance()).isEqualTo(0.0);

    // when
    account.deposit(50.0);

    // then
    assertThat(account.getBalance()).isEqualTo(50.0);
  }

  @Test
  public void deposit_ThrowsInvalidAmountException_IfAmountIsZero() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    Throwable result = catchThrowable(() -> account.deposit(0.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void deposit_ThrowsInvalidAmountException_IfAmountIsNegative() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");

    // when
    Throwable result = catchThrowable(() -> account.deposit(-50.0));

    // then
    assertThat(result).isInstanceOf(InvalidAmountException.class).hasMessage("Money amount must be greater than zero");
  }

  @Test
  public void provideInterest_IncreasesCurrentBalanceByInterest() {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");
    account.setBalance(500.0);

    // when
    account.provideInterest(); // 500.0 + 500.0 * 0.2 = 600.0

    // then
    assertThat(account.getBalance()).isEqualTo(600.0);
  }

  @Test
  public void getDetails_CorrectInformationAboutAccount() throws Exception {
    // given
    SavingsAccount account = new SavingsAccount("Peter Parker");
    account.deposit(150.0);

    // when
    String result = account.getDetails();

    // then
    assertThat(result).isEqualTo(
        "Account details:\n" +
            "Holder: Peter Parker\n" +
            "Type: savings\n" +
            "Balance: 150.0\n" +
            "Interest rate: 0.2\n"
    );
  }

}
