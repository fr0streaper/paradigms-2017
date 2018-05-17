package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public class UncheckedIntegerOperator implements TypeOperator<Integer> {
  public Integer parseNumber(String number) throws Exception {
    if (number.contains(".")) {
      throw new ParsingException("Exception: invalid integer constant " + number);
    }
    return Integer.parseInt(number);
  }

  public Integer add(Integer x, Integer y) throws OverflowException {
    return x + y;
  }

  public Integer subtract(Integer x, Integer y) throws OverflowException {
    return x - y;
  }

  public Integer multiply(Integer x, Integer y) throws OverflowException {
    return x * y;
  }

  public Integer divide(Integer x, Integer y) throws EvaluationException {
    if (y == 0) {
      throw new DivisionByZeroException();
    }
    return x / y;
  }

  public Integer min(Integer x, Integer y) throws EvaluationException {
    return Math.min(x, y);
  }

  public Integer max(Integer x, Integer y) throws EvaluationException {
    return Math.max(x, y);
  }

  public Integer count(Integer x) throws EvaluationException {
    return Integer.bitCount(x);
  }

  public Integer negate(Integer x) throws OverflowException {
    return -x;
  }
}
