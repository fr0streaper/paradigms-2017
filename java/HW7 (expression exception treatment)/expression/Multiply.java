package expression;

public class Multiply extends BinaryOperator {
  public Multiply(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) {}
  public int apply(int x, int y) {
    return x * y;
  }
}