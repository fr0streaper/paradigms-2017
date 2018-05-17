package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

public class CheckedMultiply extends BinaryOperator {
  public CheckedMultiply(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws EvaluationException {
    if (x > 0 && y > 0 && y > Integer.MAX_VALUE / x) {
      throw new OverflowException();
    }
    if (x > 0 && y < 0 && y < Integer.MIN_VALUE / x) {
      throw new OverflowException();
    }
    if (x < 0 && y > 0 && x < Integer.MIN_VALUE / y) {
      throw new OverflowException();
    }
    if (x < 0 && y < 0 && y < Integer.MAX_VALUE / x) {
      throw new OverflowException();
    }
  }

  public int apply(int x, int y) throws EvaluationException {
    check(x, y);
    return x * y;
  }
}
