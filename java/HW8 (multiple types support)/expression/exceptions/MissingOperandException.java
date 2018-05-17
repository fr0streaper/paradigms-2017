package expression.exceptions;

public class MissingOperandException extends ParsingException {
  public MissingOperandException(String expression, int index) {
    super("Exception: operand expected at non-space character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
