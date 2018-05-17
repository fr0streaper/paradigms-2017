package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public class IntegerOperator implements TypeOperator<Integer> {
  public Integer parseNumber(String number) throws Exception {
    try {
      return Integer.parseInt(number);
    } catch (Exception e) {
      throw new OverflowException();
    }
  }

  public Integer add(Integer x, Integer y) throws OverflowException {
    if ((x > 0 && y > Integer.MAX_VALUE - x) || (x < 0 && y < Integer.MIN_VALUE - x)) {
      throw new OverflowException();
    }
    return x + y;
  }

  public Integer subtract(Integer x, Integer y) throws OverflowException {
    if (x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) {
      throw new OverflowException();
    }
    if (x <= 0 && y > 0 && Integer.MIN_VALUE - x > -y) {
      throw new OverflowException();
    }
    return x - y;
  }

  public Integer multiply(Integer x, Integer y) throws OverflowException {
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
    return x * y;
  }

  public Integer divide(Integer x, Integer y) throws EvaluationException {
    if (y == 0) {
      throw new DivisionByZeroException();
    }
    if (y == -1 && x == Integer.MIN_VALUE) {
      throw new OverflowException();
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
    if (x == Integer.MIN_VALUE) {
      throw new OverflowException();
    }
    return -x;
  }
}
