package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public class UncheckedShortOperator implements TypeOperator<Short> {
  public Short parseNumber(String number) throws Exception {
    if (number.contains(".")) {
      throw new ParsingException("Exception: invalid short constant " + number);
    }
    return ((Integer)(Integer.parseInt(number))).shortValue();
  }

  public Short add(Short x, Short y) throws OverflowException {
    return ((Integer)(x + y)).shortValue();
  }

  public Short subtract(Short x, Short y) throws OverflowException {
    return ((Integer)(x - y)).shortValue();
  }

  public Short multiply(Short x, Short y) throws OverflowException {
    return ((Integer)(x * y)).shortValue();
  }

  public Short divide(Short x, Short y) throws EvaluationException {
    if (y == 0) {
      throw new DivisionByZeroException();
    }
    return ((Integer)(x / y)).shortValue();
  }

  public Short min(Short x, Short y) throws EvaluationException {
    return ((Integer)(Math.min(x, y))).shortValue();
  }

  public Short max(Short x, Short y) throws EvaluationException {
    return ((Integer)(Math.max(x, y))).shortValue();
  }

  public Short count(Short x) throws EvaluationException {
    Integer result = Integer.bitCount((int)x);
    if (x < 0)
      result -= 16;
    return result.shortValue();
  }

  public Short negate(Short x) throws OverflowException {
    return ((Integer)(-x)).shortValue();
  }
}
