package expression.exceptions;

public class DivisionByZeroException extends EvaluationException {
  public DivisionByZeroException() {
    super("division by zero");
  }
}
