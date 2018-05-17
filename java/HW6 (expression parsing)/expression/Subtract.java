package expression;

public class Subtract extends BinaryOperator {
  public Subtract(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public int apply(int x, int y) {
    return x - y;
  }
}