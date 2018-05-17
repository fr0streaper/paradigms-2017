package expression;

public class Minus extends UnaryOperator {
  public Minus(GenericExpression x) {
    super(x);
  }

  public int apply(int x) {
    return -x;
  }
}