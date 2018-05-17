package expression;

public strictfp class Divide extends BinaryOperator {
  public Divide(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public int apply(int x, int y) {
    return x / y;
  }

  public double apply(double x, double y) {
    return x / y;
  }
}