package expression.exceptions;

public class MissingOperandException extends ParsingException {
  public MissingOperandException(String expression, int index) {
    super("Exception: operand expected at character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
