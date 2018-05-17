package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

public class CheckedSubtract extends BinaryOperator {
  public CheckedSubtract(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws EvaluationException {
    if (x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) {
      throw new OverflowException();
    }
    if (x <= 0 && y > 0 && Integer.MIN_VALUE - x > -y) {
      throw new OverflowException();
    }
  }

  public int apply(int x, int y) throws EvaluationException {
    check(x, y);
    return x - y;
  }
}
