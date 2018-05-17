package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

public class CheckedNegate extends UnaryOperator {
  public CheckedNegate(GenericExpression x) {
    super(x);
  }

  public void check(int x) throws EvaluationException {
    if (x == Integer.MIN_VALUE) {
      throw new OverflowException();
    }
  }

  public int apply(int x) throws EvaluationException {
    check(x);
    return -x;
  }
}
