package expression.exceptions;

public class MissingOpenBracketException extends ParsingException {
  public MissingOpenBracketException(String expression, int index) {
    super("Exception: open bracket expected for a closing one at non-space character " + index + "\n" + expression + "\n" + printErrorPosition(index));
  }
}
