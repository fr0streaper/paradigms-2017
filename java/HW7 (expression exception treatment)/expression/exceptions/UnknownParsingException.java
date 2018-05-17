package expression.exceptions;

public class UnknownParsingException extends ParsingException {
  public UnknownParsingException(String expression, int index) {
    super("Unknown exception at character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
