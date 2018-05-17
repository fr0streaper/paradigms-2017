package expression.exceptions;

public class MissingCloseBracketException extends ParsingException {
  public MissingCloseBracketException(String expression, int index) {
    super("Exception: close bracket expected at non-space character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
