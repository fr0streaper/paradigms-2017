package expression.exceptions;

public class ParsingException extends Exception {
  public ParsingException(String message) {
    super(message);
  }

  public static String printErrorPosition(int ind) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ind; i++) {
      sb.append(' ');
    }
    sb.append('^');
    return sb.toString();
  }
}
