package expression;

public strictfp abstract class BinaryOperator implements GenericExpression {
  public final GenericExpression expression1, expression2;

  public BinaryOperator(GenericExpression first, GenericExpression second) {
    expression1 = first;
    expression2 = second;
  }

  public abstract int apply(int x, int y);
  public abstract double apply(double x, double y);

  public int evaluate(int x) {
    return apply(expression1.evaluate(x), expression2.evaluate(x));
  }

  public double evaluate(double x) {
    return apply(expression1.evaluate(x), expression2.evaluate(x));
  }

  public int evaluate(int x, int y, int z) {
    return apply(expression1.evaluate(x, y, z), expression2.evaluate(x, y, z));
  }
}