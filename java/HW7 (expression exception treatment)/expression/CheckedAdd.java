package expression;

public class CheckedAdd extends BinaryOperator {
  public CheckedAdd(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) throws ArithmeticException {
    if ((x > 0 && y > Integer.MAX_VALUE - x) || (x < 0 && y < Integer.MIN_VALUE - x)) {
      throw new ArithmeticException("overflow");
    }
  }

  public int apply(int x, int y) throws ArithmeticException {
    check(x, y);
    return x + y;
  }
}
