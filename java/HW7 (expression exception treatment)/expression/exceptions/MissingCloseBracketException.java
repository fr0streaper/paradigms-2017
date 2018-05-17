package expression.exceptions;

public class MissingCloseBracketException extends ParsingException {
  public MissingCloseBracketException(String expression, int index) {
    super("Exception: close bracket expected at character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
