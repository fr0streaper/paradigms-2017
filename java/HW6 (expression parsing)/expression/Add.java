package expression;

public class Add extends BinaryOperator {
  public Add(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public int apply(int x, int y) {
    return x + y;
  }
}