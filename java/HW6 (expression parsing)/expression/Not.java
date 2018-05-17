package expression;

public class Not extends UnaryOperator {
  public Not(GenericExpression x) {
    super(x);
  }

  public int apply(int x) {
    return ~x;
  }
}