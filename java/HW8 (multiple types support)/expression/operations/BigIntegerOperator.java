package expression.operations;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

import java.math.BigInteger;

public class BigIntegerOperator implements TypeOperator<BigInteger> {
  public BigInteger parseNumber(String number) throws Exception {
    try {
      return new BigInteger(number);
    } catch (Exception e) {
      throw new ParsingException("Exception: invalid constant " + number);
    }
  }

  public BigInteger add(BigInteger x, BigInteger y) throws OverflowException {
    return x.add(y);
  }

  public BigInteger subtract(BigInteger x, BigInteger y) throws OverflowException {
    return x.subtract(y);
  }

  public BigInteger multiply(BigInteger x, BigInteger y) throws OverflowException {
    return x.multiply(y);
  }

  public BigInteger divide(BigInteger x, BigInteger y) throws EvaluationException {
    return x.divide(y);
  }

  public BigInteger min(BigInteger x, BigInteger y) throws EvaluationException {
    return x.min(y);
  }

  public BigInteger max(BigInteger x, BigInteger y) throws EvaluationException {
    return x.max(y);
  }

  public BigInteger count(BigInteger x) throws EvaluationException {
    return BigInteger.valueOf(x.bitCount());
  }

  public BigInteger negate(BigInteger x) throws OverflowException {
    return x.negate();
  }
}
