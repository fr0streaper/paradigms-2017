package expression.exceptions;

public class InvalidVariableException extends ParsingException {
  public InvalidVariableException(String expression, int index) {
    super("Exception: invalid variable name at character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
