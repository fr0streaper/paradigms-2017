package expression.operations;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public class DoubleOperator implements TypeOperator<Double> {
  public Double parseNumber(String number) throws Exception {
    try {
      return Double.parseDouble(number);
    } catch (Exception e) {
      throw new ParsingException("Exception: invalid constant: " + number);
    }
  }

  public strictfp Double add(Double x, Double y) throws OverflowException {
    return x + y;
  }

  public strictfp Double subtract(Double x, Double y) throws OverflowException {
    return x - y;
  }

  public strictfp Double multiply(Double x, Double y) throws OverflowException {
    return x * y;
  }

  public strictfp Double divide(Double x, Double y) throws EvaluationException {
    return x / y;
  }

  public strictfp Double min(Double x, Double y) throws EvaluationException {
    return Math.min(x, y);
  }

  public strictfp Double max(Double x, Double y) throws EvaluationException {
    return Math.max(x, y);
  }

  public strictfp Double count(Double x) throws EvaluationException {
    return Double.valueOf(Long.bitCount(Double.doubleToLongBits(x)));
  }

  public strictfp Double negate(Double x) throws OverflowException {
    return -x;
  }
}
