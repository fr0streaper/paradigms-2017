package expression;

public strictfp class Subtract extends BinaryOperator {
  public Subtract(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public int apply(int x, int y) {
    return x - y;
  }

  public double apply(double x, double y) {
    return x - y;
  }
}