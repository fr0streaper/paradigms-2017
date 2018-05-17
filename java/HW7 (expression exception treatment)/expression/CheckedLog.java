package expression;

import expression.exceptions.EvaluationException;

public class CheckedLog extends BinaryOperator {
  public CheckedLog(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws EvaluationException {
    if (x <= 0 || y <= 1) {
      throw new EvaluationException("Exception: logarithm arguments must be positive; logarithm base must not be 1");
    }
  }

  public int apply(int x, int y) throws EvaluationException {
    check(x, y);

    int result = -1;
    CheckedDivide divider = new CheckedDivide(new Const(0), new Const(0));
    while (x > 0) {
      x = divider.apply(x, y);
      result++;
    }
    return result;
  }
}
