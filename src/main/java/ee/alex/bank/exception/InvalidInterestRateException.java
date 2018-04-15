package ee.alex.bank.exception;

/**
 * @author Aleksei Kulitškov
 */
public class InvalidInterestRateException extends AccountErrorException {

  private static String ERROR_MESSAGE = "Interest rate must be greater than zero";

  public InvalidInterestRateException() {
    super(ERROR_MESSAGE);
  }

}
