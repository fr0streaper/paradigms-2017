package expression;

public class Not extends UnaryOperator {
  public Not(GenericExpression x) {
    super(x);
  }

  public void check(int x) {}
  public int apply(int x) {
    return ~x;
  }
}