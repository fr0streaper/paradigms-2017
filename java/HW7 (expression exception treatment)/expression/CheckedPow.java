package expression;

import expression.exceptions.EvaluationException;

public class CheckedPow extends BinaryOperator {
  public CheckedPow(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws EvaluationException {
    if (x == 0 && y == 0) {
      throw new EvaluationException("Exception: power 0 ** 0 undefined");
    }
    if (y < 0) {
      throw new EvaluationException("Exception: power cannot be negative");
    }
  }

  public int apply(int x, int y) throws EvaluationException {
    check(x, y);

    int result = 1;
    CheckedMultiply multiply = new CheckedMultiply(new Const(0), new Const(0));
    for (int i = 0; i < y; i++) {
      result = multiply.apply(result, x);
    }
    return result;
  }
}
