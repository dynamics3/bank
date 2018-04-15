package ee.alex.bank.exception;

/**
 * @author Aleksei Kulitškov
 */
public class InvalidAmountException extends AccountErrorException {

  private static String ERROR_MESSAGE = "Money amount must be greater than zero";

  public InvalidAmountException() {
    super(ERROR_MESSAGE);
  }

}
