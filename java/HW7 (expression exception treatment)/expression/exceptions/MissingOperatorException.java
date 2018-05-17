package expression.exceptions;

public class MissingOperatorException extends ParsingException {
  public MissingOperatorException(String expression, int index) {
    super("Exception: operator expected at character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}