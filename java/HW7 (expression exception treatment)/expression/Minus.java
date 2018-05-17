package expression;

public class Minus extends UnaryOperator {
  public Minus(GenericExpression x) {
    super(x);
  }

  public void check(int x) {}
  public int apply(int x) {
    return -x;
  }
}