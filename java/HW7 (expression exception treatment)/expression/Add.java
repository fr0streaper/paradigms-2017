package expression;

public class Add extends BinaryOperator {
  public Add(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) {}
  public int apply(int x, int y) {
    return x + y;
  }
}