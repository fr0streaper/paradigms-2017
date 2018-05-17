package expression.exceptions;

public class UndefinedModeException extends Exception {
  public UndefinedModeException(String mode) {
    super("Exception: mode " + mode + " is not defined");
  }
}
