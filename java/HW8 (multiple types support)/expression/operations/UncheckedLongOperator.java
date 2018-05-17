package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public class UncheckedLongOperator implements TypeOperator<Long> {
  public Long parseNumber(String number) throws Exception {
    if (number.contains(".")) {
      throw new ParsingException("Exception: invalid long constant " + number);
    }
    return Long.parseLong(number);
  }

  public Long add(Long x, Long y) throws OverflowException {
    return x + y;
  }

  public Long subtract(Long x, Long y) throws OverflowException {
    return x - y;
  }

  public Long multiply(Long x, Long y) throws OverflowException {
    return x * y;
  }

  public Long divide(Long x, Long y) throws EvaluationException {
    if (y == 0) {
      throw new DivisionByZeroException();
    }
    return x / y;
  }

  public Long min(Long x, Long y) throws EvaluationException {
    return Math.min(x, y);
  }

  public Long max(Long x, Long y) throws EvaluationException {
    return Math.max(x, y);
  }

  public Long count(Long x) throws EvaluationException {
    return Long.valueOf(Long.bitCount(x));
  }

  public Long negate(Long x) throws OverflowException {
    return -x;
  }
}
