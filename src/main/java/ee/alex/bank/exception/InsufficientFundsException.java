package ee.alex.bank.exception;

/**
 * @author Aleksei Kulitškov
 */
public class InsufficientFundsException extends AccountErrorException {

  private static String ERROR_MESSAGE = "Insufficient funds";

  public InsufficientFundsException() {
    super(ERROR_MESSAGE);
  }

}
