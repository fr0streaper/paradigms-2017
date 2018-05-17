package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends BinaryOperator {
  public CheckedDivide(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws EvaluationException {
    if (y == 0) {
      throw new DivisionByZeroException();
    }

    if (y == -1 && x == Integer.MIN_VALUE) {
      throw new OverflowException();
    }
  }

  public int apply(int x, int y) throws EvaluationException {
    check(x, y);
    return x / y;
  }
}
