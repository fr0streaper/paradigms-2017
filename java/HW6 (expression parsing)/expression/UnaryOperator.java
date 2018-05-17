package expression;

public abstract class UnaryOperator implements GenericExpression {
  GenericExpression expression;

  public UnaryOperator(GenericExpression x) {
    expression = x;
  }

  public abstract int apply(int x);

  public int evaluate(int x, int y, int z) {
    return apply(expression.evaluate(x, y, z));
  }
}