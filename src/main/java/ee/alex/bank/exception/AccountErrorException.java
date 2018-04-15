package ee.alex.bank.exception;

/**
 * @author Aleksei Kulitškov
 */
public class AccountErrorException extends Exception {

  private static final long serialVersionUID = 9207900019763116146L;

  public AccountErrorException(String message) {
    super(message);
  }

}
