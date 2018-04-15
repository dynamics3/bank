package ee.alex.bank.exception;

/**
 * @author Aleksei Kulitškov
 */
public class LimitReachedException extends AccountErrorException {

  private static String ERROR_MESSAGE = "Limit reached";

  public LimitReachedException() {
    super(ERROR_MESSAGE);
  }

}
