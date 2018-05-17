package expression;

public class Xor extends BinaryOperator {
  public Xor(GenericExpression first, GenericExpression second) {
    super(first, second);
  }

  public void check(int x, int y) {}
  public int apply(int x, int y) {
    return x ^ y;
  }
}